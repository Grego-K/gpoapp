package gr.aueb.cf.gpoapp.mapper;

import gr.aueb.cf.gpoapp.dto.ProductDTO;
import gr.aueb.cf.gpoapp.dto.RebateTierDTO;
import gr.aueb.cf.gpoapp.model.Product;
import gr.aueb.cf.gpoapp.model.ProductProgress;
import gr.aueb.cf.gpoapp.model.RebateTier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDTO mapToProductDTO(Product product, ProductProgress progress) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();

        // Βασικά Πεδία Προϊόντος
        dto.setId(product.getId());
        dto.setUuid(product.getUuid());
        dto.setProductName(product.getProductName());
        dto.setDescription(product.getDescription());
        dto.setBasePrice(product.getBasePrice());
        dto.setGpoPrice(product.getGpoPrice());
        dto.setStockQuantity(product.getStockQuantity());

        // Mapping Category (Χρήση .getName())
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        // Mapping Supplier (Χρήση .getCompanyName())
        if (product.getSupplier() != null) {
            dto.setSupplierId(product.getSupplier().getId());
            dto.setSupplierName(product.getSupplier().getCompanyName());
        }

        // --- Rebate & Progress Logic (Null-Safe) ---

        // Volume από το Progress (αν δεν υπάρχει εγγραφή για την περίοδο, βάζουμε 0)
        int currentVol = (progress != null) ? progress.getVolume() : 0;
        dto.setCurrentVolume(currentVol);

        // Mapping των Tiers στη λίστα του DTO
        if (product.getRebateTiers() != null && !product.getRebateTiers().isEmpty()) {
            dto.setRebateTiers(product.getRebateTiers().stream()
                    .map(this::mapToRebateTierDTO)
                    .collect(Collectors.toList()));

            // Συλλογή όλων των ποσών εκπτώσεων ταξινομημένα
            List<BigDecimal> amounts = product.getRebateTiers().stream()
                    .map(RebateTier::getRebateAmount)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            dto.setAllRebateAmounts(amounts);

            // Υπολογισμός επόμενου στόχου και ποσοστού μπάρας
            calculateRebateProgress(dto, product, currentVol);
        }

        return dto;
    }

    private void calculateRebateProgress(ProductDTO dto, Product product, int currentVol) {
        // 1. Εύρεση της τρέχουσας έκπτωσης (Current Rebate)
        product.getRebateTiers().stream()
                .filter(t -> currentVol >= t.getMinQuantity() && (t.getMaxQuantity() == null || currentVol <= t.getMaxQuantity()))
                .findFirst()
                .ifPresent(t -> dto.setCurrentRebateLabel(t.getRebateAmount().stripTrailingZeros().toPlainString() + "€"));

        // 2. Εύρεση του Tier στο οποίο ανήκουμε ή του αμέσως επόμενου για το Threshold
        product.getRebateTiers().stream()
                .filter(t -> t.getMaxQuantity() == null || t.getMaxQuantity() >= currentVol)
                .findFirst()
                .ifPresentOrElse(tier -> {
                    // Ο στόχος είναι το MaxQuantity του επιπέδου που διανύουμε
                    int target = (tier.getMaxQuantity() != null) ? tier.getMaxQuantity() : tier.getMinQuantity();
                    dto.setNextTierThreshold(target);
                    dto.setNextRebateLabel("-" + tier.getRebateAmount() + "€");

                    // Υπολογισμός % προόδου για το CSS width της μπάρας
                    double percent = ((double) currentVol / target) * 100;
                    dto.setProgressPercent((int) Math.min(percent, 100));
                }, () -> {
                    // Αν έχουμε ξεπεράσει και το τελευταίο Tier
                    dto.setNextTierThreshold(currentVol);
                    dto.setProgressPercent(100);
                    dto.setNextRebateLabel("MAX REBATE REACHED");
                });
    }

    private RebateTierDTO mapToRebateTierDTO(RebateTier tier) {
        return new RebateTierDTO(
                tier.getMinQuantity(),
                tier.getMaxQuantity(),
                tier.getRebateAmount(),
                tier.getPeriodType().name()
        );
    }
}
package gr.aueb.cf.gpoapp.mapper;

import gr.aueb.cf.gpoapp.dto.ProductDTO;
import gr.aueb.cf.gpoapp.dto.RebateTierDTO;
import gr.aueb.cf.gpoapp.model.Product;
import gr.aueb.cf.gpoapp.model.ProductProgress;
import gr.aueb.cf.gpoapp.model.RebateTier;
import org.springframework.stereotype.Component;

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
        if (product.getRebateTiers() != null) {
            dto.setRebateTiers(product.getRebateTiers().stream()
                    .map(this::mapToRebateTierDTO)
                    .collect(Collectors.toList()));

            // Υπολογισμός επόμενου στόχου και ποσοστού μπάρας
            calculateRebateProgress(dto, product, currentVol);
        }

        return dto;
    }

    private void calculateRebateProgress(ProductDTO dto, Product product, int currentVol) {
        product.getRebateTiers().stream()
                .filter(t -> t.getMinQuantity() > currentVol)
                .findFirst()
                .ifPresentOrElse(nextTier -> {
                    dto.setNextTierThreshold(nextTier.getMinQuantity());
                    dto.setNextRebateLabel("-" + nextTier.getRebateAmount() + "€");

                    // Υπολογισμός % προόδου για το CSS width της μπάρας
                    double percent = ((double) currentVol / nextTier.getMinQuantity()) * 100;
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
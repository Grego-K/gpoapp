package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.model.*;
import gr.aueb.cf.gpoapp.repository.ProductProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RebateSettlementService {

    private final ProductProgressRepository progressRepository;

    /**
     * Υπολογίζει το rebate για ένα συγκεκριμένο OrderItem.
     * Αν ο όγκος ξεπεράσει το τελευταίο tier, διατηρεί την ανώτατη έκπτωση.
     * Πχ tier1 έστω από 50 εως 100τμχ  με έκπτωση 5, tier2=last απο 101 εως 200τμχ με έκπτωση 7, για κάθε τμχ>200 ισχύει έκπτωση 7.
     */
    public BigDecimal calculateItemRebate(OrderItem item) {
        try {
            Product product = item.getProduct();
            String currentPeriod = getCurrentPeriodLabel();

            // Διαβάζουμε το τρέχον volume από το repository
            int currentTotalVolume = progressRepository
                    .findByProductIdAndPeriodLabel(product.getId(), currentPeriod)
                    .map(ProductProgress::getVolume)
                    .orElse(0);

            // Ταξινομούμε τα tiers βάσει ποσότητας
            List<RebateTier> sortedTiers = product.getRebateTiers().stream()
                    .sorted(Comparator.comparing(RebateTier::getMinQuantity))
                    .collect(Collectors.toList());

            BigDecimal rebatePerUnit = BigDecimal.ZERO;

            if (!sortedTiers.isEmpty()) {
                for (RebateTier t : sortedTiers) {
                    // Αν ο συνολικός όγκος έχει φτάσει τουλάχιστον το ελάχιστο αυτού του tier
                    if (currentTotalVolume >= t.getMinQuantity()) {
                        rebatePerUnit = t.getRebateAmount();

                        // Αν είμαστε μέσα στα όρια αυτού του tier (ή αν είναι το τελευταίο open-ended),
                        // αυτό είναι το rebate μας. Αν ο όγκος είναι μεγαλύτερος από το max,
                        // το loop συνεχίζει στο επόμενο tier για μεγαλύτερη έκπτωση.
                        if (t.getMaxQuantity() != null && currentTotalVolume <= t.getMaxQuantity()) {
                            break;
                        }
                    }
                }
            }

            return rebatePerUnit.multiply(BigDecimal.valueOf(item.getQuantity()));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private String getCurrentPeriodLabel() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int quarter = (month - 1) / 3 + 1;
        return year + "_Q" + quarter;
    }
}
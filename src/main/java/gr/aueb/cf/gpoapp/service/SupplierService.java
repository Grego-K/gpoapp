package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.model.Supplier;
import gr.aueb.cf.gpoapp.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService implements ISupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findAllSuppliers() {
        // Καλούμε το Repository για να πάρουμε τους προμηθευτές από τη βάση
        return supplierRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier findSupplierById(Long id) throws Exception {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new Exception("Ο προμηθευτής δεν βρέθηκε"));
    }
}
package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.model.Supplier;
import java.util.List;

public interface ISupplierService {
    /**
     * Επιστρέφει όλους τους προμηθευτές.
     */
    List<Supplier> findAllSuppliers();

    /**
     * Εύρεση προμηθευτή βάσει ID.
     */
    Supplier findSupplierById(Long id) throws Exception;
}
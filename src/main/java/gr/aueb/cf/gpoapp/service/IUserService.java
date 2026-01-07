package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.gpoapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.gpoapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.gpoapp.dto.UserInsertDTO;
import gr.aueb.cf.gpoapp.model.User;

public interface IUserService {
    /**
     * @throws EntityAlreadyExistsException Αν το username/email υπάρχει ήδη
     * @throws EntityNotFoundException Αν το Region ID δεν αντιστοιχεί σε εγγραφή
     * @throws EntityInvalidArgumentException Γενικότερο
     */
    User registerUser(UserInsertDTO userInsertDTO)
            throws EntityAlreadyExistsException, EntityNotFoundException, EntityInvalidArgumentException;
}
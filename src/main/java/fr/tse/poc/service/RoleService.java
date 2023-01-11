package fr.tse.poc.service;

import fr.tse.poc.dao.RoleRepository;
import fr.tse.poc.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepo;

    /**
     * Return a role by its id
     * @param idRole The id of the role we're looking for
     * @return the role if it exists, else return null
     */
    public Role findRole(long idRole){
        return roleRepo.findById(idRole).orElse(null);
    }
}

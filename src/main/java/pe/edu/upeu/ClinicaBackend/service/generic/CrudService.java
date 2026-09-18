package pe.edu.upeu.ClinicaBackend.service.generic;

import java.util.List;

public interface CrudService<REQ, RES, ID> {
    RES create(REQ request);
    RES read(ID id);
    RES update(ID id, REQ request);
    void delete(ID id);
    List<RES> readAll();
}
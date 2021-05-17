package de.hsrm.mi.web.projekt.foto;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.utils.FotoBearbeitungService;

@Service
public class FotoServiceImpl implements FotoService {

  @Autowired FotoBearbeitungService fbservice;
  @Autowired FotoRepository fotoRepository;

  public Foto fotoAbspeichern(Foto foto) {
    fbservice.aktualisiereMetadaten(foto);
    fbservice.orientiereFoto(foto);
    return foto;
  }

  public Optional<Foto> fotoAbfragenNachId(Long id) {
    return null;
  }

  public List<Foto> alleFotosNachZeitstempelSortiert() {
    return fotoRepository.findAll();
  }

  public void loescheFoto(Long id) {}
}

package de.hsrm.mi.web.projekt.foto;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.utils.FotoBearbeitungService;

@Service
public class FotoServiceImpl implements FotoService {

  @Autowired FotoBearbeitungService fbservice;
  @Autowired FotoRepository fotoRepository;

  public Foto fotoAbspeichern(Foto foto) {
    fbservice.aktualisiereMetadaten(foto);
    fbservice.orientiereFoto(foto);
    Foto gemanagetesFoto = fotoRepository.save(foto);
    return gemanagetesFoto;
  }

  public Optional<Foto> fotoAbfragenNachId(Long id) {
    Optional<Foto> optionalFoto = fotoRepository.findById(id);
    return optionalFoto;
  }

  public List<Foto> alleFotosNachZeitstempelSortiert() {
    return fotoRepository.findAll(Sort.by("zeitstempel"));
  }

  public void loescheFoto(Long id) {
    fotoRepository.deleteById(id);
  }
}

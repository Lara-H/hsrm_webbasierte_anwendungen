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
  @Autowired FotoRepository fotorepo;

  public Foto fotoAbspeichern(Foto foto) {
    fbservice.aktualisiereMetadaten(foto);
    fbservice.orientiereFoto(foto);
    Optional<String> adresse = new AdressServiceImpl().findeAdresse(foto.getGeobreite(), foto.getGeolaenge());
    foto.setOrt(adresse.get());
    Foto gemanagetesFoto = fotorepo.save(foto);
    return gemanagetesFoto;
  }

  public Optional<Foto> fotoAbfragenNachId(Long id) {
    Optional<Foto> optionalFoto = fotorepo.findById(id);
    return optionalFoto;
  }

  public List<Foto> alleFotosNachZeitstempelSortiert() {
    return fotorepo.findAll(Sort.by("zeitstempel"));
  }

  public void loescheFoto(Long id) {
    fotorepo.deleteById(id);
  }

  public void fotoKommentieren(long id, String autor, String kommentar) {
    try {
      fotorepo.findById(id).get().getKommentare().add(new Kommentar(autor, kommentar));
    }
    catch (Exception e) {
      new NoSuchElementException();
    }
  }

  public List<Kommentar> alleKommentareFuerFoto(long fotoid) {
    try {
      return fotorepo.findById(fotoid).get().getKommentare();
    }
    catch (Exception e) {
      new NoSuchElementException();
    }
    return null;
  }

  public void fotoKommentarLoeschen(long fotoid, long kid) {
    try {
      fotorepo.findById(fotoid).get().getKommentare().remove((int) kid);
    }
    catch (Exception e) {
      new NoSuchElementException();
    }
  }
}

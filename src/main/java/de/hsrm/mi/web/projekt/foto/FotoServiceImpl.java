package de.hsrm.mi.web.projekt.foto;

import java.util.List;
import java.util.NoSuchElementException;
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

  public void fotoKommentieren(long id, String autor, String kommentar) throws NoSuchElementException {
      Foto foto = fotorepo.findById(id).get();
      foto.addKommentar(new Kommentar(autor, kommentar));
      fotorepo.save(foto);
  }

  public List<Kommentar> alleKommentareFuerFoto(long fotoid) throws NoSuchElementException {
      return fotorepo.findById(fotoid).get().getKommentare();
  }

  public void fotoKommentarLoeschen(long fotoid, long kid) throws NoSuchElementException {
    Foto foto = fotorepo.findById(fotoid).get();
    
    if(foto.findKommentarById(kid) == null){
      throw new NoSuchElementException();
    }else{
      foto.removeKommentar(foto.findKommentarById(kid));
  }   
  }
}

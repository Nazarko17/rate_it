package nazar.sekh.rate_it.services;

import nazar.sekh.rate_it.dao.TitleDAO;
import nazar.sekh.rate_it.models.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class TitleService {
    @Autowired
    TitleDAO titleDAO;

    public Title findById(int id) {
        return titleDAO.findTitleById(id);
    }

    public <T> List<Title> findByKeyword(T keyword) {
        return titleDAO.findByKeyword(keyword);
    }

    public List<Title> findAll(){
        return titleDAO.findAll();
    }

    public Title saveTitle(Title title){
        return titleDAO.save(title);
    }

    public void transferFile(MultipartFile file) {
        String pathToFolder = System.getProperty("user.home") + File.separator + "images" + File.separator;
        try {
            file.transferTo(new File(pathToFolder + file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

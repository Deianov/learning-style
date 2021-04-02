package bg.geist.service;

import bg.geist.constant.enums.CloudinaryTags;
import bg.geist.domain.model.service.CloudImageModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    CloudImageModel uploadImage(MultipartFile multipartFile, String publicId, CloudinaryTags tags) throws IOException;
}
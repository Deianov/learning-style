package bg.geist.service;

import bg.geist.constant.enums.CloudinaryTags;
import bg.geist.domain.model.service.CloudImageModel;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private static final String TEMP_FILE = "temp-file";
    private static final String URL = "url";
    private static final String PUBLIC_ID = "public_id";
    private static final String TAGS = "tags";


    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public CloudImageModel uploadImage(MultipartFile multipartFile, String publicId, CloudinaryTags tags) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return new CloudImageModel(null, null);
        }
        Map<String, String> opts = new HashMap<>();
        if (publicId != null) {
            opts.put(PUBLIC_ID, publicId);
        }
        if (tags != null) {
            opts.put(TAGS, tags.toString());
        }

        File file = File.createTempFile(TEMP_FILE, multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        Map result = this.cloudinary.uploader().upload(file, opts);
        return new CloudImageModel(result.get(PUBLIC_ID).toString(), result.get(URL).toString());
    }
}
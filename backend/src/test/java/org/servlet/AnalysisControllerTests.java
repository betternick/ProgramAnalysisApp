package org.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AnalysisControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalysisController fileUploadController;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void changeDir() {
        AnalysisController.UPLOAD_DIR = tempDir.toAbsolutePath().toString();
    }

    @Test
    public void testUploadFile_Success() throws Exception {
        Resource resource = new ClassPathResource("testUpload.txt");
        MultipartFile multipartFile = new MockMultipartFile("testUpload.txt", resource.getInputStream());
        ResponseEntity<Boolean> response = fileUploadController.uploadFile(multipartFile);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());

    }

    @Test
    public void testPostFile_Success() throws Exception {
        byte[] fileContent = "Hello, World!".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "multipart/form-data", fileContent);

        mockMvc.perform(multipart("/upload")
                .file(file))
                .andExpect(status().isOk());

        // Additional assertions if needed
    }

    @Test
    public void testPostFile_NoFile() throws Exception {
        mockMvc.perform(multipart("/upload"))
                .andExpect(status().isBadRequest());
    }
}

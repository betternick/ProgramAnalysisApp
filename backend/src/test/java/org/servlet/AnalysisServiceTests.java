package org.servlet;

import org.graph.CFGBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalysisServiceTests {

    private final AnalysisService service = new AnalysisService();

    @TempDir
    Path tempDir;
    @BeforeEach
    public void changeDir() {
        AnalysisService.UPLOAD_DIR = tempDir.toAbsolutePath().toString();
    }
    @Test
    public void testUploadFile_Success() throws Exception{
        Resource resource = new ClassPathResource("testUpload.txt");
        MultipartFile multipartFile = new MockMultipartFile("testUpload.txt", resource.getInputStream());
        MockedStatic<CFGBuilder> builder = Mockito.mockStatic(CFGBuilder.class);
        Boolean response = service.uploadFile(multipartFile);
        assertEquals(true, response);

    }

    @Test
    public void testUploadFile_Fail() throws Exception{
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        Mockito.when(multipartFile.getInputStream()).thenThrow(new IOException());
        Boolean response = service.uploadFile(multipartFile);
        assertEquals(false, response);

    }


}

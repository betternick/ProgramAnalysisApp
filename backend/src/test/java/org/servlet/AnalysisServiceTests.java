package org.servlet;

import org.graph.CFGBuilder;
import org.graph.CFGConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class AnalysisServiceTests {
    @Mock
    private CFGBuilder builder;

    @InjectMocks
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

        MockedStatic<CFGConverter> converter = Mockito.mockStatic(CFGConverter.class);
        converter.when(() -> CFGConverter.convertAllCFGs(Mockito.anyMap())).thenReturn("success");

        Mockito.doNothing().when(builder).buildCFGs(Mockito.anyString());
        builder.globalCFGMap = new HashMap<>();

        String response = service.uploadFile(multipartFile);
        assertEquals("success", response);

    }

    @Test
    public void testUploadFile_Fail() throws Exception{
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        Mockito.when(multipartFile.getInputStream()).thenThrow(new IOException());
        try {
            String response = service.uploadFile(multipartFile);
            fail();
        } catch (IOException e) {
             // expected
        }

    }


}

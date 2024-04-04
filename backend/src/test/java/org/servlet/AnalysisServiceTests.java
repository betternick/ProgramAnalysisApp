package org.servlet;

import org.analysis.Analyser;
import org.analysis.ExecTreeStats;
import org.graph.CFGBuilder;
import org.graph.CFGConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.profile.Executor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

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
    public void testUploadFile_Success() throws Exception {
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
    public void testUploadFile_Fail() throws Exception {
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

    @Test
    public void testExecute_Success() {
        Mockito.doNothing().when(builder).serializeMap(Mockito.anyString());
        Mockito.doReturn(List.of(1)).when(builder).getAllNodeIds();
        Executor mockexe = Mockito.mock(Executor.class);
        MockedStatic<Executor> executor = Mockito.mockStatic(Executor.class);
        executor.when(() -> Executor.getInstance()).thenReturn(mockexe);

        ExecTreeStats stats = new ExecTreeStats(1, 1.0, 2.0, 3.0);

        try(MockedConstruction<Analyser> mockPaymentService = Mockito.mockConstruction(Analyser.class,(mock,context)-> {
            Mockito.doNothing().when(mock).analyze(Mockito.anyString());
            Mockito.doReturn(stats).when(mock).getStat(Mockito.anyInt());
        })) {

            service.filePath = "examples/Simple.java";
            var map = service.executeFile();
            assertEquals(1, map.size());
            assertEquals(stats, map.get(1));
        }


    }

    @Test
    public void testExecute_Fail() {
        Mockito.doNothing().when(builder).serializeMap(Mockito.anyString());
        Mockito.doReturn(List.of(1)).when(builder).getAllNodeIds();
        Executor mockexe = Mockito.mock(Executor.class);
        MockedStatic<Executor> executor = Mockito.mockStatic(Executor.class);
        executor.when(() -> Executor.getInstance()).thenReturn(mockexe);
        Mockito.doThrow(new RuntimeException()).when(mockexe).execute(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString());

        try {
            service.filePath = "examples/Simple.java";
            service.executeFile();
            fail();
        } catch (RuntimeException e) {
            // expected
        }


    }

    @Test
    public void testGetClassName_Success() {
        String className = service.getClassName("examples/Simple.java");
        assertEquals("src.main.resources.Simple", className);
    }
}

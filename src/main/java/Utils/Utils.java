package Utils;

import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {
    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }
    public static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = Utils.class.getResourceAsStream(fileName)) {
            if (in == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
                result = scanner.useDelimiter("\\A").next();
            }
        }
        return result;
    }
    public static List<String> readAllLines(String fileName) {
        List<String> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Class.forName(Utils.class.getName()).getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null){
                result.add(line);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

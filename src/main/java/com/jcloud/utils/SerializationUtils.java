package com.jcloud.utils;

import org.springframework.core.ConfigurableObjectInputStream;

import java.io.*;

/**
 * @author jiaxm
 * @date 2020/12/24
 */
public class SerializationUtils {


    public static byte[] serialize(Object state) {
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(state);
            oos.flush();
            return bos.toByteArray();
        }
        catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        finally {
            if (oos != null) {
                try {
                    oos.close();
                }
                catch (IOException e) {
                    // eat it
                }
            }
        }
    }

    public static <T> T deserialize(byte[] byteArray) {
        ObjectInputStream oip = null;
        try {
            oip = new ConfigurableObjectInputStream(new ByteArrayInputStream(byteArray),
                    Thread.currentThread().getContextClassLoader());
            @SuppressWarnings("unchecked")
            T result = (T) oip.readObject();
            return result;
        }
        catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        finally {
            if (oip != null) {
                try {
                    oip.close();
                }
                catch (IOException e) {
                    //
                }
            }
        }
    }

}

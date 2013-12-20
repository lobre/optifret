package libs;

import java.io.File;
import java.io.FilenameFilter;

/**
 * FilenameFilter qui accepte uniquement les fichiers .xml
 */
public class CustomFilenameFilter implements FilenameFilter {
    private String ext;

    public CustomFilenameFilter(String ext){
        this.ext = ext.toLowerCase();
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(ext);
    }

}
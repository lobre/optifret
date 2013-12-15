package libs;

import java.io.File;
import java.io.FilenameFilter;

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
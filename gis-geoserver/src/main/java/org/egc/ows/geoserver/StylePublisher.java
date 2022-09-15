package org.egc.ows.geoserver;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * publish styles (*.sld)
 * @author houzhiwei
 */
public class StylePublisher extends GSManager {

    Logger log = LoggerFactory.getLogger(StylePublisher.class);

    public boolean publishStyle(File sldFile) throws FileNotFoundException, IOException {
        boolean b = false;
        String name = FilenameUtils.getBaseName(sldFile.getName());
        if (!reader.existsStyle(name)) {
            b = publisher.publishStyle(sldFile, name);
            log.debug("Publishing style " + sldFile + " as " + name);
            System.out.println("Publishing style " + sldFile + " as " + name);
        }
        return b;
    }

    public boolean publishStyleInWorkspace(String workspace, String sldFilePath) {
        boolean b = false;
        File sldFile = new File(sldFilePath);
        String name = FilenameUtils.getBaseName(sldFilePath);
        if (!reader.existsStyle(name)) {
            b = publisher.publishStyleInWorkspace(workspace, sldFile, name);
            log.debug("Publishing style " + sldFile + " as " + name + " in workspace " + workspace);
            System.out.println("Publishing style " + sldFile + " as " + name);
        }
        return b;
    }

    public boolean publishStyles(String sldFilesDir) throws FileNotFoundException, IOException {
        boolean b = false;
        File sldDir = new File(sldFilesDir);
        for (File sldFile : sldDir.listFiles((FilenameFilter) new SuffixFileFilter(".sld"))) {
            System.out.println("Existing styles: " + reader.getStyles().getNames());
            String basename = FilenameUtils.getBaseName(sldFile.toString());
            System.out.println("Publishing style " + sldFile + " as " + basename);
            b = publisher.publishStyle(sldFile, basename);
        }
        return b;
    }
}

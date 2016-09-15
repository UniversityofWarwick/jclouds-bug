import java.io.File;
import java.util.Properties;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Swift {

    public static void main(String[] args) throws Exception {

        Logger logger = LoggerFactory.getLogger(Swift.class);

        String provider = "openstack-swift";

        Iterable<Module> modules = ImmutableSet
                .<Module>of(new SLF4JLoggingModule());

        Properties properties = new Properties();
        properties.load(Swift.class.getResourceAsStream("/swift.properties"));
        String container = properties.getProperty("container");

        BlobStoreContext context = ContextBuilder.newBuilder(provider)
                .modules(modules).overrides(properties)
                .build(BlobStoreContext.class);
        BlobStore blobStore = context.getBlobStore();

        // upload a file
        logger.info("Uploading file");
        String filename = properties.getProperty("upload_blob");
        File file = new File(filename);
        org.jclouds.blobstore.domain.Blob jcloudsBlob =
                blobStore.blobBuilder(filename)
                        .payload(file)
                        .contentType("text/plain")
                        .build();
        blobStore.putBlob(container, jcloudsBlob);

        long sleepInterval = Long.parseLong(properties.getProperty("sleep"));
        logger.info("\n\nsleeping for {} seconds...\n", sleepInterval);
        Thread.sleep(sleepInterval * 1000L);

        // upload another file
        logger.info("Uploading file");
        filename = properties.getProperty("upload_blob");
        file = new File(filename);
        jcloudsBlob = blobStore.blobBuilder(filename)
                .payload(file)
                .contentType("text/plain")
                .build();
        blobStore.putBlob(container, jcloudsBlob);

    }

}

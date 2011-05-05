package org.resthub.core.service.audit;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.model.StandaloneEntity;
import org.resthub.core.service.StandaloneEntityService;

/**
 * Perform tests on audit feature
 * 
 * @author bmeurant <Baptiste Meurant>
 * 
 */
public class AuditTest {

    private static final int NB_THREADS = 20;

    @Inject
    @Named("standaloneEntityService")
    private StandaloneEntityService standaloneEntityService;

    /**
     * Custom ThreadFactory that provide a callback for any exception occuring
     * in a thread create with this factory
     * 
     * @author bmeurant <Baptiste Meurant>
     */
    static class AuditThreadFactory implements ThreadFactory {

        /**
         * {@InheritDoc}
         */
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);

            // define a custom exception handler for exceptions that were not
            // caught by thread. In our case, we want to react on the occurence
            // of a ClassCastException caused by non thread safe audit feature
            t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(final Thread t, final Throwable e) {
                    if (e instanceof ClassCastException) {
                        // In the case of the tested error case,
                        // interruption of ThreadGroup in order to alert
                        // parent thread (unit test)
                        t.getThreadGroup().interrupt();
                    }
                }
            });
            return t;
        }
    }

    /**
     * Test the occurrence of a known "non thread safe" problem on audit
     * feature.
     * 
     * Related to bug #40 {@link http
     * ://bitbucket.org/ilabs/resthub/issue/40/orgresthubbookingmodeluser
     * -cannot-be-cast}
     * 
     */
    @Test
    public void multipleGetAndFind() {

        // define task to execute getMethod
        Runnable getWorker = new Runnable() {

            @Override
            public void run() {
                // execute and try to cast result : that can produce error
                @SuppressWarnings("unused")
                StandaloneEntity entity = (StandaloneEntity) standaloneEntityService
                        .findById(1L);
            }
        };

        // define task to execute findMethod
        Runnable findWorker = new Runnable() {

            @Override
            public void run() {
                // execute and try to cast result : that can produce error
                @SuppressWarnings("unused")
                List<StandaloneEntity> entities = (List<StandaloneEntity>) standaloneEntityService
                        .findAll();
            }
        };

        // repeat execution because occuring of the error is not systematic : it
        // depends on timing and concurrency context. But error occurred
        // systematically at least one time on several executions
        for (int i = NB_THREADS; i > 0; i--) {

            // Use a custom ThreadFactory in order to be able to do something
            // when the expected audit-related exception occured in one of the
            // child thread
            ExecutorService executor = Executors.newFixedThreadPool(2,
                    new AuditThreadFactory());

            executor.execute(getWorker);
            executor.execute(findWorker);

            // This will make the executor accept no new threads
            // and finish all existing threads in the queue
            executor.shutdown();

            // Wait until all threads are finish, a timeout occurs or thread is
            // interrupted
            try {
                executor.awaitTermination(NB_THREADS, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                // One thread received an exception : error occurred, tais
                // failed
                // Force test failure
                fail("Concurrency error on audit feature !!");
            }
        }
    }
}

package org.resthub.common.service;

import org.fest.assertions.api.Assertions;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.resthub.common.model.Sample;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CrudServiceTest {

    private PagingAndSortingRepository<Sample, Long> sampleRepository = mock(PagingAndSortingRepository.class);
    private CrudServiceImpl<Sample, Long, PagingAndSortingRepository<Sample, Long>> sampleService;

    private Sample sample;
    private List<Sample> allSamples = new ArrayList<Sample> ();

    @BeforeClass
    public void setup() {
        this.sample = new Sample("testSample");
        this.sample.setId(1L);

        Sample secondSample = new Sample("testSample2");
        secondSample.setId(2L);

        this.allSamples.add(this.sample);
        this.allSamples.add(secondSample);

        this.sampleService = new CrudServiceImpl<Sample, Long, PagingAndSortingRepository<Sample, Long>>();
        this.sampleService.setRepository(this.sampleRepository);
    }

    @BeforeMethod
    public void setupTest() {
        reset(this.sampleRepository);

        Answer<Sample> answerSingle = new Answer<Sample>() {
            @Override
            public Sample answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (Sample) args[0];
            }
        };

        when(this.sampleRepository.findOne(1L)).thenReturn(sample);
        when(this.sampleRepository.save(any(Sample.class))).thenAnswer(answerSingle);
        when(this.sampleRepository.findAll()).thenReturn(this.allSamples);
        when(this.sampleRepository.findAll(anySetOf(Long.class))).thenReturn(this.allSamples);
        when(this.sampleRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<Sample>(this.allSamples));
        when(this.sampleRepository.count()).thenReturn((long) this.allSamples.size());
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testCreateNull() {
        this.sampleService.create(null);
    }

    @Test
    public void testCreate() {
        Sample toCreate = new Sample("sample-create");
        Sample created = this.sampleService.create(toCreate);

        verify(this.sampleRepository).save(toCreate);
        Assertions.assertThat(created).isNotNull().isEqualTo(toCreate);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testUpdateNull() {
        this.sampleService.update(null);
    }

    @Test
    public void testUpdate() {
        Sample toUpdate = new Sample("sample-update");
        Sample updated = this.sampleService.update(toUpdate);

        verify(this.sampleRepository).save(toUpdate);
        Assertions.assertThat(updated).isNotNull().isEqualTo(toUpdate);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testDeleteNull() {
        Sample toDelete = null;
        this.sampleService.delete(toDelete);
    }

    @Test
    public void testDelete() {
        Sample toDelete = new Sample("sample-delete");
        this.sampleService.delete(toDelete);

        verify(this.sampleRepository).delete(toDelete);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testDeleteIdNull() {
        Long toDeleteId = null;
        this.sampleService.delete(toDeleteId);
    }

    @Test
    public void testDeleteId() {
        this.sampleService.delete(1L);

        verify(this.sampleRepository).delete(1L);
    }

    @Test
    public void testDeleteAll() {
        this.sampleService.deleteAll();

        verify(this.sampleRepository).deleteAll();
    }

    @Test
    public void testDeleteAllWithCascade() {
        this.sampleService.deleteAllWithCascade();

        verify(this.sampleRepository).findAll();
        verify(this.sampleRepository, times(this.allSamples.size())).delete(any(Sample.class));
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testFindByIdNull() {
        this.sampleService.findById(null);
    }

    @Test
    public void testFindById() {
        Sample sample = this.sampleService.findById(1L);

        verify(this.sampleRepository).findOne(1L);
        Assertions.assertThat(sample).isNotNull().isEqualTo(this.sample);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testFindByIdsNull() {
        this.sampleService.findByIds(null);
    }

    @Test
    public void testFindByIds() {
        Set<Long> ids = new HashSet<Long>(Arrays.asList(1L, 2L));
        Iterable<Sample> samples = this.sampleService.findByIds(ids);

        verify(this.sampleRepository).findAll(ids);
        Assertions.assertThat(samples).isNotNull().isEqualTo(this.allSamples);
    }

    @Test
    public void testFindAll() {
        Iterable<Sample> samples = this.sampleService.findAll();

        verify(this.sampleRepository).findAll();
        Assertions.assertThat(samples).isNotNull().isEqualTo(this.allSamples);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testFindAllPageRequestNull() {
        PageRequest pageRequest = null;
        this.sampleService.findAll(pageRequest);
    }

    @Test
    public void testFindAllPageRequest() {
        PageRequest pageRequest = new PageRequest(1, 1);
        Page<Sample> samplePage = this.sampleService.findAll(pageRequest);

        verify(this.sampleRepository).findAll(pageRequest);
        Assertions.assertThat(samplePage).isNotNull();
        Assertions.assertThat(samplePage.getContent()).isNotNull().isEqualTo(this.allSamples);
    }

    @Test
    public void testCount() {
        Long count = this.sampleService.count();

        verify(this.sampleRepository).count();
        Assertions.assertThat(count).isNotNull().isEqualTo(this.allSamples.size());
    }

}

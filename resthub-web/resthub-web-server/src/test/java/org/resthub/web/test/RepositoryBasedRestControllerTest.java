package org.resthub.web.test;

import org.fest.assertions.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.resthub.common.exception.NotFoundException;
import org.resthub.web.controller.SampleRepositoryBasedRestController;
import org.resthub.web.model.Sample;
import org.resthub.web.repository.SampleResourceRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RepositoryBasedRestControllerTest {

    private SampleResourceRepository sampleRepository = mock(SampleResourceRepository.class);
    private SampleRepositoryBasedRestController sampleController;

    private Sample sample;
    private List<Sample> allSamples = new ArrayList<Sample>();

    @BeforeClass
    public void setup() {
        this.sample = new Sample("testSample");
        this.sample.setId(1L);

        Sample secondSample = new Sample("testSample2");
        secondSample.setId(2L);

        this.allSamples.add(this.sample);
        this.allSamples.add(secondSample);

        this.sampleController = new SampleRepositoryBasedRestController();
        this.sampleController.setRepository(this.sampleRepository);
    }

    @BeforeMethod
    public void setupTest() {
        reset(this.sampleRepository);

        Answer<Sample> answerSingle = new Answer<Sample>() {
            @Override
            public Sample answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Sample sample = (Sample) args[0];
                if (new Long(2).equals(sample.getId())) return null;
                return sample;
            }
        };

        when(this.sampleRepository.findOne(1L)).thenReturn(sample);
        when(this.sampleRepository.save(any(Sample.class))).thenAnswer(answerSingle);
        when(this.sampleRepository.findAll()).thenReturn(this.allSamples);
        when(this.sampleRepository.findAll(anySetOf(Long.class))).thenReturn(this.allSamples);
        when(this.sampleRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<Sample>(this.allSamples));
        when(this.sampleRepository.count()).thenReturn((long) this.allSamples.size());
    }

    @Test
    public void testCreate() {
        Sample toCreate = new Sample("sample-create");
        Sample created = this.sampleController.create(toCreate);

        verify(this.sampleRepository).save(toCreate);
        Assertions.assertThat(created).isNotNull().isEqualTo(toCreate);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testUpdateNull() {
        this.sampleController.update(null, null);
    }

    @Test
    public void testUpdate() {
        Sample toUpdate = new Sample("sample-update");
        toUpdate.setId(1L);
        Sample updated = this.sampleController.update(toUpdate.getId(), toUpdate);

        verify(this.sampleRepository).save(toUpdate);
        Assertions.assertThat(updated).isNotNull().isEqualTo(toUpdate);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testUpdateNotFound() {
        Sample toUpdate = new Sample("sample-update");
        toUpdate.setId(2L);
        this.sampleController.update(toUpdate.getId(), toUpdate);
    }

    @Test
    public void testFindAll() {
        Iterable<Sample> samples = this.sampleController.findAll();

        verify(this.sampleRepository).findAll();
        Assertions.assertThat(samples).isNotNull().isEqualTo(this.allSamples);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindPaginatedZeroPage() {
        this.sampleController.findPaginated(0, 10, "", null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindPaginatedInvalidPage() {
        this.sampleController.findPaginated(-1, 10, "", null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindPaginatedInvalidDirection() {
        this.sampleController.findPaginated(1, 10, "ascordesc", null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindPaginatedNullProperties() {
        this.sampleController.findPaginated(1, 10, "asc", null);
    }

    @Test
    public void testFindPaginated() {
        ArgumentCaptor<PageRequest> pageRequest = ArgumentCaptor.forClass(PageRequest.class);

        this.sampleController.findPaginated(1, 10, "", null);
        verify(this.sampleRepository).findAll(pageRequest.capture());
        Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(0);
        Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(10);

        this.sampleController.findPaginated(5, 2, "", null);
        verify(this.sampleRepository, times(2)).findAll(pageRequest.capture());
        Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(4);
        Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(2);
    }

    @Test
    public void testFindPaginatedDirection() {
        ArgumentCaptor<PageRequest> pageRequest = ArgumentCaptor.forClass(PageRequest.class);

        this.sampleController.findPaginated(1, 10, "asc", "id,name");
        verify(this.sampleRepository).findAll(pageRequest.capture());
        Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(0);
        Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(10);
        Assertions.assertThat(pageRequest.getValue().getSort()).isNotNull();
        Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id")).isNotNull();
        Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id").getDirection()).isNotNull().isEqualTo(Sort.Direction.ASC);
        Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("name")).isNotNull();
        Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("name").getDirection()).isNotNull().isEqualTo(Sort.Direction.ASC);

        this.sampleController.findPaginated(5, 2, "desc", "id,name");
        verify(this.sampleRepository, times(2)).findAll(pageRequest.capture());
        Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(4);
        Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(2);
        Assertions.assertThat(pageRequest.getValue().getSort()).isNotNull();
        Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id")).isNotNull();
        Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id").getDirection()).isNotNull().isEqualTo(Sort.Direction.DESC);
        Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("name")).isNotNull();
        Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("name").getDirection()).isNotNull().isEqualTo(Sort.Direction.DESC);
    }

    @Test
    public void testFindById() {
        Sample sample = this.sampleController.findById(1L);

        verify(this.sampleRepository).findOne(1L);
        Assertions.assertThat(sample).isNotNull().isEqualTo(this.sample);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testFindByIdNotFound() {
        this.sampleController.findById(2L);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testFindByIdsNull() {
        this.sampleController.findByIds(null);
    }

    @Test
    public void testFindByIds() {
        Set<Long> ids = new HashSet<Long>(Arrays.asList(1L, 2L));
        Iterable<Sample> samples = this.sampleController.findByIds(ids);

        verify(this.sampleRepository).findAll(ids);
        Assertions.assertThat(samples).isNotNull().isEqualTo(this.allSamples);
    }

    @Test
    public void testDelete() {
        this.sampleController.delete();

        verify(this.sampleRepository).findAll();
        verify(this.sampleRepository, times(this.allSamples.size())).delete(any(Sample.class));
    }

    @Test
    public void testDeleteId() {
        this.sampleController.delete(1L);

        verify(this.sampleRepository).delete(this.sample);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testDeleteIdNotFound() {
        this.sampleController.delete(2L);
    }

}

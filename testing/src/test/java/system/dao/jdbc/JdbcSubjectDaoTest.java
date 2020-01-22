package system.dao.jdbc;

import com.epam.testing.system.dao.interfaces.SubjectDao;
import com.epam.testing.system.dao.jdbc.JdbcSubjectDao;
import com.epam.testing.system.entities.Subject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class JdbcSubjectDaoTest {
    private SubjectDao subjectDao = new JdbcSubjectDao();

    @Test
    void insertSubjectTest() {
        Subject subject = new Subject();
        subject.setSubject("test_subj");
        subject.setId(subjectDao.insertSubject(subject));
        Assert.assertFalse(subject.getId() == -1);
    }

    @Test
    void getSubjectTest() {
        Assert.assertNotNull(subjectDao.getSubject("test_subj"));
    }
}

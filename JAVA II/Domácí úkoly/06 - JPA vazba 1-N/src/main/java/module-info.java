module cz.vsb.fei.java2.du06 {
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires com.h2database;

    opens cz.vsb.fei.java2.du06.model to org.hibernate.orm.core;
}

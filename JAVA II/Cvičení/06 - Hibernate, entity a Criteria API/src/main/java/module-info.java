module cz.vsb.fei.java2.lab06_module {
	requires java.sql;
	requires org.apache.logging.log4j;
	requires static lombok;
	requires jakarta.persistence;
	requires com.h2database;
	requires org.hibernate.orm.core;

    requires static jakarta.annotation;

	opens lab.data;

}

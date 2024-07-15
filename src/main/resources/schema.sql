CREATE TABLE application (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             title VARCHAR(255),
                             text VARCHAR(1000)
);

CREATE TABLE application_tags (
                                  application_id BIGINT,
                                  tags VARCHAR(255),
                                  PRIMARY KEY (application_id, tags),
                                  FOREIGN KEY (application_id) REFERENCES application(id)
);
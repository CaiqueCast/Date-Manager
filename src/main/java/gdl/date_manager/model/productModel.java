package gdl.date_manager.model;

import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

public class productModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Date validity;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false, foreignKey = @ForeignKey(name = "FK_PRODUCT_USER"))
    private UserModel user;

}

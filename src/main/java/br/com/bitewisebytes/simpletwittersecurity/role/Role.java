package br.com.bitewisebytes.simpletwittersecurity.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    private String name;


    public enum Values{
        ADMIN(1L),
        BASIC(2L);


        long roleID;

        Values(long roleID){
            this.roleID = roleID;
        }

        public long getRoleId() {
            return roleID;
        }
    }
}

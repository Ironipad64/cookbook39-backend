package com.lambdaschool.cookbook.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;


/**
 * The entity allowing interaction with the users table
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "users")
@JsonIgnoreProperties(value = {"roles"})
@ApiModel
public class User
        extends Auditable {

    /**
     * The primary key (long) of the users table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "the id of the user", value = "positive integer number to identify this user",
            notes = "ID is auto-generated by the database")
    private long userid;

    /**
     * The username (String). Cannot be null and must be unique
     */
    @Column(nullable = false, unique = true)
    @ApiModelProperty(name = "the user's username", value = "the username of the user", required = true,
            notes = "this can look however the user wants it to")
    private String username;

    /**
     * The password (String) for this user. Cannot be null. Never get displayed
     */
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(name = "user's password",
            value = "the full password. The backend will encrypt the password on creation", required = true,
            notes = "WRITE ONLY. This will not appear in any endpoint response.")
    private String password;

//	@Column(columnDefinition = "TEXT")
//	@Lob
//	@ApiModelProperty(notes = "profile picture URL (String) for the user's profile picture. Will default to a " +
//	                          "silhouette unless otherwise specified.")
//	@Column(nullable = false, length = 10000)

    @Lob
    @Column(nullable = false)
    private String profilepicture = "https://www.cmcaindia.org/wp-content/uploads/2015/11/default-profile-picture-gmail-2.png";

    /**
     * Primary email account of user. Could be used as the userid. Cannot be null and must be unique.
     */
    @Column(nullable = false, unique = true)
    @Email
    @ApiModelProperty(
            notes = "primary email address of user. Cannot be null; must be unique; must match the shape of a " +
                    "standard email address.")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "recipe", allowSetters = true)
    @ApiModelProperty(notes = "the recipes that this user has POSTED.")
    private List<Recipe> recipes = new ArrayList<>();

    /**
     * Part of the join relationship between user and role
     * connects users to the user role combination
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = "user", allowSetters = true)
    @ApiModelProperty(
            notes = "entirely handled by the backend. But these roles discern who gets to access what " + " in the backend.")
    private Set<UserRoles> roles = new HashSet<>();

    /**
     * Default constructor used primarily by the JPA.
     */
    public User() {
    }

    /**
     * Given the params, create a new user object
     * <p>
     * userid is autogenerated
     *
     * @param username       The username (String) of the user
     * @param password       The password (String) of the user
     * @param email          The primary email (String) of the user
     * @param profilepicture The profilepicture url for the user
     */
    public User(
            String username,
            String password,
            String email,
            String profilepicture
    ) {
        setUsername(username);
        setPassword(password);
        this.email = email;
        this.profilepicture = profilepicture;
    }

    /**
     * Given the params, create a new user object
     * <p>
     * userid is autogenerated.
     *
     * @param username The username (String) of the user
     * @param password The password (String) of the user
     * @param email    The email address (String, email format) of the user
     */
    public User(
            String username,
            String password,
            String email
    ) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }


    /**
     * Getter for userid
     *
     * @return the userid (long) of the user
     */
    public long getUserid() {
        return userid;
    }

    /**
     * Setter for userid. Used primary for seeding data
     *
     * @param userid the new userid (long) of the user
     */
    public void setUserid(long userid) {
        this.userid = userid;
    }

    /**
     * Getter for username
     *
     * @return the username (String) lowercase
     */
    public String getUsername() {
        return username;
    }

    /**
     * setter for username
     *
     * @param username the new username (String) converted to lowercase
     */
    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    /**
     * getter for primary email
     *
     * @return the primary email (String) for the user converted to lowercase
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter for primary email
     *
     * @param email the new primary email (String) for the user (will be converted to lowercase)
     */
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    /**
     * Getter for the password
     *
     * @return the password (String) of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the new password (String) for this user. Comes in plain text and goes out encrypted
     */
    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void setPasswordNoEncrypt(String password) {
        this.password = password;
    }

    /**
     * Getter for user role combinations
     *
     * @return A list of user role combinations associated with this user
     */
    public Set<UserRoles> getRoles() {
        return roles;
    }

    /**
     * Setter for user role combinations
     *
     * @param roles Change the list of user role combinations associated with this user to this one
     */
    public void setRoles(Set<UserRoles> roles) {
        this.roles = roles;
    }


    /**
     * Getter for the list of recipes POSTED by this user
     *
     * @return A list of recipes posted by this user.
     */
    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Setter for the user's list of recipes.
     *
     * @param recipes Change the recipes associated with this user to the given list.
     */
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    /**
     * Getter for the profile picture URL
     *
     * @return the URL for this user's profile picture
     */
    public String getProfilepicture() {
        return profilepicture;
    }

    /**
     * Setter for the profile picture URL
     *
     * @param profilepicture the URL (String) to set for this user's profile picture
     */
    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    /**
     * Getter for the createdDate (property of super class Auditable)
     *
     * @return The Date at which this user was created.
     */
    public Date getCreatedDate() {
        return super.createdDate;
    }

    /**
     * Internally, user security requires a list of authorities, roles, that the user has. This method is a simple way to provide those.
     * Note that SimpleGrantedAuthority requests the format ROLE_role name all in capital letters!
     *
     * @return The list of authorities, roles, this user object has
     */
    @JsonIgnore
    public List<SimpleGrantedAuthority> getAuthority() {
        List<SimpleGrantedAuthority> rtnList = new ArrayList<>();

        for (UserRoles r : this.roles) {
            String myRole = "ROLE_" + r.getRole()
                    .getName()
                    .toUpperCase();
            rtnList.add(new SimpleGrantedAuthority(myRole));
        }

        return rtnList;
    }


}

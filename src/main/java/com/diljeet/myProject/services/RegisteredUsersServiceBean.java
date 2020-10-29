/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diljeet.myProject.services;

import com.diljeet.myProject.customexceptions.UserAccountDoesNotExistException;
import com.diljeet.myProject.interfaces.RegisteredUsersService;
import com.diljeet.myProject.entities.RegisteredUsers;
import com.diljeet.myProject.utils.MyProjectUtils;
import static com.diljeet.myProject.utils.MyProjectUtils.manipulateEncodedPassword;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.mail.Session;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 *
 * @author diljeet
 */
@Stateless
//@RolesAllowed("Administrator")
public class RegisteredUsersServiceBean implements RegisteredUsersService {

    private static final Logger logger = Logger.getLogger(RegisteredUsersServiceBean.class.getCanonicalName());
//    private static final Logger LOG = LoggerFactory.getLogger(RegisteredUsersServiceBean.class.getCanonicalName());

    @PersistenceContext(name = "my-persistence-unit")
    private EntityManager em;

    @Resource(lookup = "java:jboss/mail/gmailSession")
    public Session mailSession;

    @Override
    public Response createUser(RegisteredUsers user) throws NoSuchAlgorithmException {
        if (user == null) {
            return null;
        } else {
            try {
                Query query = em.createQuery("Select u FROM RegisteredUsers u where u.username = :username");
                query.setParameter("username", user.getUsername());
                List<RegisteredUsers> userExists = query.getResultList();

                if (!userExists.isEmpty()) {
                    return Response.status(Response.Status.FOUND).build();
                } else {
                    if (user.getPassword().equals(user.getConfirmPassword())) {

                        byte[] salt = MyProjectUtils.generateSalt();
                        byte[] password = user.getPassword().getBytes();

                        MessageDigest md = MessageDigest.getInstance("SHA-512");
                        md.update(salt);
                        md.update(password);
                        byte[] digest = md.digest();

                        Encoder encoder = Base64.getEncoder();

                        String encodedSalt = encoder.encodeToString(salt);
                        user.setSalt(encodedSalt);

                        String encodedPassword = encoder.encodeToString(digest);
                        user.setPassword(encodedPassword);

                        if (user.getRole() == null) {
                            user.setRole("Administrator");
                        }

                        if (user.getIsActive() == null) {
                            user.setIsActive("no");
                        }

                        if (user.getDateCustomerCreated() == null) {
                            user.setDateCustomerCreated(new Date());
                        }

                        em.persist(user);
                        MyProjectUtils.sendActivationLinkOnMail(mailSession, user.getUsername());

                    } else {
                        return Response.status(Response.Status.PRECONDITION_FAILED).build();
                    }

                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Exception is {0}", e.getMessage());
//                LOG.error(e.getMessage());
                return null;
            }

            return Response.status(Response.Status.CREATED).build();

        }

    }

    @Override
    public List<RegisteredUsers> getAllUsers() {
        //logger.log(Level.SEVERE, "Coming from service");
        List<RegisteredUsers> users = null;
        try {
            users = em.createNamedQuery("getAllUsers").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override    
//    @RolesAllowed("Administrator")
    public Response getUser(String username) {        
        try {
            Query query = em.createQuery("Select u FROM RegisteredUsers u where u.username = :username");
            query.setParameter("username", username);
            Object accountExists = query.getSingleResult();
            RegisteredUsers existingUser = (RegisteredUsers) accountExists;
            if (existingUser instanceof RegisteredUsers) {                
                return Response.status(Response.Status.FOUND).entity(existingUser).build();
            } else {                
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch(Exception e){            
            return null;
        }          
    }

    @Override
    public void activateAccount(String encodedEmail,
            HttpServletRequest req,
             HttpServletResponse res
    ) {
        try {
            Decoder decoder = Base64.getDecoder();
            byte[] decodedEmail = decoder.decode(encodedEmail);
            String username = new String(decodedEmail);

            Query query = em.createQuery("Select u FROM RegisteredUsers u where u.username = :username");
            query.setParameter("username", username);
            Object accountExists = query.getSingleResult();
            RegisteredUsers existingUser = (RegisteredUsers) accountExists;

            if (existingUser instanceof RegisteredUsers) {
                if ((existingUser.getIsActive()).equals("no")) {
                    existingUser.setIsActive("yes");
                    res.sendRedirect(req.getContextPath() + "/login.xhtml?account=true");
                } else {
                    res.sendRedirect(req.getContextPath() + "/login.xhtml?isactive=true");
                }
            } else {
                logger.log(Level.SEVERE, "Account does not Exist");
//                LOG.error("Account does not Exist");
                res.sendRedirect(req.getContextPath() + "/login.xhtml?account=false");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            logger.log(Level.SEVERE, e.getMessage());
//            LOG.error(e.getMessage());
        }
    }

    @Override
    public Response forgotPassword(String username) {
        try {
            Query query = em.createQuery("Select u FROM RegisteredUsers u where u.username = :username");
            query.setParameter("username", username);
            Object accountExists = query.getSingleResult();
            RegisteredUsers existingUser = (RegisteredUsers) accountExists;
            if (existingUser instanceof RegisteredUsers) {
                if ((existingUser.getIsActive()).equals("yes")) {
                    String encodedSalt = existingUser.getSalt();
                    String password = MyProjectUtils.generateForgotPassword();

                    Decoder decoder = Base64.getDecoder();

                    byte[] saltInBytes = decoder.decode(encodedSalt);
                    byte[] passwordInBytes = password.getBytes();
                    MessageDigest md = MessageDigest.getInstance("SHA-512");
                    md.update(saltInBytes);
                    md.update(passwordInBytes);
                    byte[] digest = md.digest();
                    Encoder encoder = Base64.getEncoder();
                    String encodedPassword = encoder.encodeToString(digest);
                    existingUser.setPassword(encodedPassword);

                    existingUser.setDateCustomerLastUpdated(new Date());

                    MyProjectUtils.sendForgotPasswordOnMail(mailSession, username, password);

                    return Response.status(Response.Status.OK).build();

                } else {
                    return Response.status(Response.Status.NOT_MODIFIED).build();
                }
            } else {
//                LOG.error("User not Found");
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            if (e instanceof NoResultException) {
//                LOG.error("User not Found");
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
//                logger.log(Level.SEVERE, e.getMessage());
//                LOG.error(e.getMessage());
                return null;
            }
        }

    }

    @Override
    public Response getUserByUsername(String username, RegisteredUsers user
    ) {
        try {
            Query query = em.createQuery("Select u FROM RegisteredUsers u where u.username = :username");
            query.setParameter("username", username);
            Object accountExists = query.getSingleResult();
            RegisteredUsers existingUser = (RegisteredUsers) accountExists;

            if ((existingUser instanceof RegisteredUsers)
                    && (existingUser.getIsActive()).equals("yes")) {
                existingUser.setIsPasswordChangeRequest("yes");
                if ((user.getPassword()).equals(user.getConfirmPassword())) {
                    String encodedSalt = existingUser.getSalt();

                    Decoder decoder = Base64.getDecoder();

                    byte[] saltInBytes = decoder.decode(encodedSalt);
                    byte[] passwordInBytes = user.getPassword().getBytes();
                    MessageDigest md = MessageDigest.getInstance("SHA-512");
                    md.update(saltInBytes);
                    md.update(passwordInBytes);
                    byte[] digest = md.digest();

                    if ((existingUser.getIsPasswordChangeRequest()).equals("yes")) {
                        MyProjectUtils.sendChangePasswordConsentLinkOnMail(mailSession, username, digest);
                    }

                    return Response.status(Response.Status.ACCEPTED).build();
                } else {
                    return Response.status(Response.Status.PRECONDITION_FAILED).build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            if (e instanceof NoResultException) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
//                logger.log(Level.SEVERE, e.getMessage());
//                LOG.error(e.getMessage());
                return null;
            }
        }
    }

    @Override
    public void changePassword(String encodedEmail,
            String encodedPassword,
             HttpServletRequest req,
            HttpServletResponse res
    ) {
        try {
            Decoder decoder = Base64.getDecoder();
            byte[] decodedEmail = decoder.decode(encodedEmail);
            String username = new String(decodedEmail);

            Query query = em.createQuery("Select u FROM RegisteredUsers u where u.username = :username");
            query.setParameter("username", username);
            Object accountExists = query.getSingleResult();
            RegisteredUsers existingUser = (RegisteredUsers) accountExists;
            if ((existingUser instanceof RegisteredUsers)
                    && (existingUser.getIsActive()).equals("yes")
                    && (existingUser.getIsPasswordChangeRequest()).equals("yes")) {
                if (encodedPassword.contains("_")) {
                    encodedPassword = manipulateEncodedPassword(encodedPassword, "_", "/");
                }
                existingUser.setPassword(encodedPassword);
                existingUser.setIsPasswordChangeRequest("no");
                existingUser.setDateCustomerLastUpdated(new Date());
                res.sendRedirect(req.getContextPath() + "/login.xhtml?passwordChanged=true");
            } else if ((existingUser.getIsPasswordChangeRequest()).equals("no")) {
                res.sendRedirect(req.getContextPath() + "/login.xhtml?passwordAlreadyChanged=true");
            } else {
                res.sendRedirect(req.getContextPath() + "/login.xhtml?account=false");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            logger.log(Level.SEVERE, e.getMessage());
//            LOG.error(e.getMessage());
        }
    }

}

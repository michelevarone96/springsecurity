package com.mvarone.springsecurity.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlUserDetailsService implements UserDetailsService {

    private final InMemoryUserDetailsManager delegate;

    public XmlUserDetailsService() {
        this.delegate = new InMemoryUserDetailsManager(loadUsersFromXml());
    }

    private List<UserDetails> loadUsersFromXml() {
        List<UserDetails> users = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("users.xml")) {
            var doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            var nodeList = doc.getElementsByTagName("user");
            for (int i = 0; i < nodeList.getLength(); i++) {
                var element = (org.w3c.dom.Element) nodeList.item(i);
                String username = element.getElementsByTagName("username").item(0).getTextContent();
                String password = element.getElementsByTagName("password").item(0).getTextContent();
                String rolesStr = element.getElementsByTagName("roles").item(0).getTextContent();
                System.out.println("Caricato utente: " + username + " con password hash: " + password);
                String[] roles = rolesStr.split(",");
                UserDetails user = User.builder()
                        .username(username)
                        .password(password)
                        .roles(roles)
                        .build();
                users.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore nel caricamento utenti da XML", e);
        }
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return delegate.loadUserByUsername(username);
    }
}

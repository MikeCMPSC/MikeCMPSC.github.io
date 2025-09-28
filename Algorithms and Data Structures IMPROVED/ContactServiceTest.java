package test;

import main.Contact;
import main.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContactServiceTest {

    private ContactService contactService;

    @BeforeEach
    void setUp() {
        contactService = new ContactService();
        contactService.addContact(new Contact("C1", "Alice", "Smith", "1234567890", "123 Main St"));
        contactService.addContact(new Contact("C2", "Bob", "Jones", "0987654321", "456 Oak Ave"));
    }

    @Test
    void testAddContact() {
        Contact newContact = new Contact("C3", "Charlie", "Brown", "5555555555", "789 Pine Rd");
        contactService.addContact(newContact);

        assertEquals(newContact, contactService.getContactById("C3"));
    }

    @Test
    void testAddDuplicateContactThrowsException() {
        Contact duplicateContact = new Contact("C1", "Alice", "Smith", "1234567890", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> contactService.addContact(duplicateContact));
    }

    @Test
    void testDeleteContact() {
        contactService.deleteContact("C2");
        assertNull(contactService.getContactById("C2"));
    }

    @Test
    void testDeleteNonexistentContactThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> contactService.deleteContact("C999"));
    }

    @Test
    void testUpdateContact() {
        contactService.updateContact("C1", "Alicia", "Smithers", "2223334444", "987 Elm St");
        Contact updatedContact = contactService.getContactById("C1");

        assertAll(
            () -> assertEquals("Alicia", updatedContact.getFirstName()),
            () -> assertEquals("Smithers", updatedContact.getLastName()),
            () -> assertEquals("2223334444", updatedContact.getPhone()),
            () -> assertEquals("987 Elm St", updatedContact.getAddress())
        );
    }

    @Test
    void testUpdateNonexistentContactThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            contactService.updateContact("C999", "Test", "User", "0000000000", "Nowhere"));
    }
}

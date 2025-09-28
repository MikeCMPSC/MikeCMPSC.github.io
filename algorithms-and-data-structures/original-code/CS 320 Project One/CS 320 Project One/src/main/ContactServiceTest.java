package main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactServiceTest {

    private ContactService contactService;

    @BeforeEach
    void setUp() {
        contactService = new ContactService();
    }

    // 1. Test to verify adding contacts with unique IDs
    @Test
    void testAddContact() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);

        assertAll("Add Contact",
            () -> assertEquals(contact, contactService.getContactById("1234567890"), "Contact should be added correctly"),
            () -> assertThrows(IllegalArgumentException.class, 
                () -> contactService.addContact(new Contact("1234567890", "Jane", "Smith", "0987654321", "456 Elm St")), 
                "Adding a contact with a duplicate ID should throw an exception")
        );
    }

    // 2. Test to verify deleting contacts by contactId
    @Test
    void testDeleteContact() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);

        contactService.deleteContact("1234567890");
        assertAll("Delete Contact",
            () -> assertNull(contactService.getContactById("1234567890"), "Contact should be deleted successfully")
        );
    }

    // 3. Test to verify updating contact fields by contactId
    @Test
    void testUpdateContact() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);

        contactService.updateContact("1234567890", "Jane", "Smith", "0987654321", "456 Elm St");

        Contact updatedContact = contactService.getContactById("1234567890");
        assertAll("Update Contact",
            () -> assertEquals("Jane", updatedContact.getFirstName(), "First name should be updated"),
            () -> assertEquals("Smith", updatedContact.getLastName(), "Last name should be updated"),
            () -> assertEquals("0987654321", updatedContact.getPhone(), "Phone number should be updated"),
            () -> assertEquals("456 Elm St", updatedContact.getAddress(), "Address should be updated")
        );
    }
}

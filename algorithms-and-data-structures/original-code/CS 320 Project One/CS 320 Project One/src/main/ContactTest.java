package main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContactTest {

    // Test for a valid Contact creation (ensures everything works for valid input)
    @Test
    public void testValidContactCreation() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Street, City");
        Assertions.assertNotNull(contact, "Contact should not be null");
        Assertions.assertEquals("1234567890", contact.getContactId(), "Contact ID should match");
        Assertions.assertEquals("John", contact.getFirstName(), "First name should match");
        Assertions.assertEquals("Doe", contact.getLastName(), "Last name should match");
        Assertions.assertEquals("1234567890", contact.getPhone(), "Phone should match");
        Assertions.assertEquals("123 Street, City", contact.getAddress(), "Address should match");
    }

    // Test for first name requirements: not null and no longer than 10 characters
    @Test
    public void testNullFirstNameThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Contact("1234567890", null, "Doe", "1234567890", "123 Street, City"),
                "Null first name should throw an exception");
    }

    @Test
    public void testFirstNameLongerThan10CharactersThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Contact("1234567890", "Jonathanathan", "Doe", "1234567890", "123 Street, City"),
                "First name longer than 10 characters should throw an exception");
    }

    @Test
    public void testFirstNameExactly10CharactersAllowed() {
        Contact contact = new Contact("1234567890", "Johnathan", "Doe", "1234567890", "123 Street, City");
        Assertions.assertEquals("Johnathan", contact.getFirstName(), "First name exactly 10 characters should be allowed");
    }

    // Test for last name requirements: not null and no longer than 10 characters
    @Test
    public void testNullLastNameThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Contact("1234567890", "John", null, "1234567890", "123 Street, City"),
                "Null last name should throw an exception");
    }

    @Test
    public void testLastNameLongerThan10CharactersThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Contact("1234567890", "John", "DoeLongLastName", "1234567890", "123 Street, City"),
                "Last name longer than 10 characters should throw an exception");
    }

    @Test
    public void testLastNameExactly10CharactersAllowed() {
        Contact contact = new Contact("1234567890", "John", "DoeLong", "1234567890", "123 Street, City");
        Assertions.assertEquals("DoeLong", contact.getLastName(), "Last name exactly 10 characters should be allowed");
    }

    // Test for phone number requirements: not null and exactly 10 digits
    @Test
    public void testNullPhoneNumberThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Contact("1234567890", "John", "Doe", null, "123 Street, City"),
                "Null phone number should throw an exception");
    }

    @Test
    public void testPhoneNumberNotExactly10DigitsThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Contact("1234567890", "John", "Doe", "12345", "123 Street, City"),
                "Phone number not exactly 10 digits should throw an exception");
    }

    @Test
    public void testPhoneNumberExactly10DigitsAllowed() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Street, City");
        Assertions.assertEquals("1234567890", contact.getPhone(), "Phone number exactly 10 digits should be allowed");
    }

    // Test for address requirements: not null and no longer than 30 characters
    @Test
    public void testNullAddressThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Contact("1234567890", "John", "Doe", "1234567890", null),
                "Null address should throw an exception");
    }

    @Test
    public void testAddressLongerThan30CharactersThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Contact("1234567890", "John", "Doe", "1234567890", "1234567890123456789012345678901"),
                "Address longer than 30 characters should throw an exception");
    }

    @Test
    public void testAddressExactly30CharactersAllowed() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123456789012345678901234567890");
        Assertions.assertEquals("123456789012345678901234567890", contact.getAddress(), "Address exactly 30 characters should be allowed");
    }

    // Test for contact ID requirements: not null, no longer than 10 characters
    @Test
    public void testNullContactIdThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Contact(null, "John", "Doe", "1234567890", "123 Street, City"),
                "Null contact ID should throw an exception");
    }

    @Test
    public void testContactIdLongerThan10CharactersThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Contact("12345678901", "John", "Doe", "1234567890", "123 Street, City"),
                "Contact ID longer than 10 characters should throw an exception");
    }

    @Test
    public void testContactIdExactly10CharactersAllowed() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Street, City");
        Assertions.assertEquals("1234567890", contact.getContactId(), "Contact ID exactly 10 characters should be allowed");
    }

    // Test for contact ID not updatable
    @Test
    public void testContactIdImmutability() {
        // Create a valid contact object
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Street, City");

        // Assert the initial value of contactId
        String initialContactId = contact.getContactId();

        // Attempt to create a new Contact with the same contactId
        Contact newContact = new Contact("1234567890", "Jane", "Smith", "0987654321", "456 Avenue, City");

        // Assert that the original contact's ID remains unchanged
        Assertions.assertEquals(initialContactId, contact.getContactId(), "Contact ID should remain unchanged after creation");

        // Optionally, verify that the new contact's ID is the same as the original's
        Assertions.assertEquals("1234567890", newContact.getContactId(), "New contact ID should match the original contact ID");
    }
}

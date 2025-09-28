package main;

import java.util.HashMap;
import java.util.Map;

public class ContactService {

    private final Map<String, Contact> contactMap = new HashMap<>();

    // Method to add a new contact
    public void addContact(Contact contact) {
        if (contactMap.containsKey(contact.getContactId())) {
            throw new IllegalArgumentException("Contact ID already exists");
        }
        contactMap.put(contact.getContactId(), contact);
    }

    // Method to delete a contact by contactId
    public void deleteContact(String contactId) {
        if (!contactMap.containsKey(contactId)) {
            throw new IllegalArgumentException("Contact ID not found");
        }
        contactMap.remove(contactId);
    }

    // Method to update contact fields by contactId
    public void updateContact(String contactId, String newFirstName, String newLastName, String newPhone, String newAddress) {
        Contact contact = contactMap.get(contactId);
        if (contact == null) {
            throw new IllegalArgumentException("Contact ID not found");
        }
        contact.setFirstName(newFirstName);
        contact.setLastName(newLastName);
        contact.setPhone(newPhone);
        contact.setAddress(newAddress);
    }

    // Method to get contact by ID (for testing purposes)
    public Contact getContactById(String contactId) {
        return contactMap.get(contactId);
    }
}

package nieldw.example.matchers.factory;

import nieldw.example.matchers.dto.ContactDetailsDTO;
import nieldw.example.matchers.dto.PersonDTO;
import nieldw.example.matchers.entity.ContactDetails;
import nieldw.example.matchers.entity.Person;
import org.junit.Test;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static nieldw.example.matchers.builder.dto.ContactDetailsDTOBuilder.aContactDetailsDTO;
import static nieldw.example.matchers.builder.dto.PersonDTOBuilder.aPersonDTO;
import static nieldw.example.matchers.builder.entity.ContactDetailsBuilder.aContactDetails;
import static nieldw.example.matchers.builder.entity.PersonBuilder.aPerson;
import static nieldw.example.matchers.matcher.HasContactDetailsDTOBeenCreatedCorrectly.hasSameStateAs;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

public class ContactDetailsDtoFactoryTest {

    private static final long CONTACT_DETAILS_ID = 222L;
    private static final long PERSON_ID = 333L;
    private static final String SOME_TELEPHONE_NUMBER = "some telephone number";
    private static final String SOME_CELLPHONE_NUMBER = "some cellphone number";
    private static final String SOME_POSTAL_ADDRESS = "some postal address";
    private static final String SOME_HOME_ADDRESS = "some home address";
    private static final String SOME_PERSON_NAME = "some person name";

    private ContactDetailsDtoFactory contactDetailsDtoFactory;

    @Test
    public void testBuildThinContactDetailsDTO() throws Exception {
        // Set up fixture
        contactDetailsDtoFactory = new ContactDetailsDtoFactory(new PersonDtoFactory());

        ContactDetails contactDetails = aContactDetails()
                .withId(CONTACT_DETAILS_ID)
                .withPersonId(PERSON_ID)
                .withTelephoneNumber(SOME_TELEPHONE_NUMBER)
                .withCellphoneNumber(SOME_CELLPHONE_NUMBER)
                .withPostalAddress(SOME_POSTAL_ADDRESS)
                .withHomeAddress(SOME_HOME_ADDRESS)
                .build();
        ContactDetailsDTO expectedContactDetails = aContactDetailsDTO()
                .withId(CONTACT_DETAILS_ID)
                .withPersonId(PERSON_ID)
                .withTelephoneNumber(SOME_TELEPHONE_NUMBER)
                .withCellphoneNumber(SOME_CELLPHONE_NUMBER)
                .withPostalAddress(SOME_POSTAL_ADDRESS)
                .withHomeAddress(SOME_HOME_ADDRESS)
                .build();

        // Exercise SUT
        ContactDetailsDTO returnedContactDetailsDTO = contactDetailsDtoFactory.buildThinContactDetailsDTO(contactDetails);

        // Verify behaviour
        assertThat(returnedContactDetailsDTO, hasSameStateAs(expectedContactDetails));
    }

    /**
     * Illustrate shazamcrest with nested object (PersonDTO).
     */
    @Test
    public void testBuild_shouldAlsoPopulatePersonDTO() {
        // Set up fixture
        contactDetailsDtoFactory = new ContactDetailsDtoFactory(new PersonDtoFactory());

        Person person = aPerson()
                .withId(PERSON_ID)
                .withName(SOME_PERSON_NAME)
                .build();
        ContactDetails contactDetails = aContactDetails()
                .withId(CONTACT_DETAILS_ID)
                .withPersonId(PERSON_ID)
                .withTelephoneNumber(SOME_TELEPHONE_NUMBER)
                .withCellphoneNumber(SOME_CELLPHONE_NUMBER)
                .withPostalAddress(SOME_POSTAL_ADDRESS)
                .withHomeAddress(SOME_HOME_ADDRESS)
                .build();
        ContactDetailsDTO expectedContactDetails = aContactDetailsDTO()
                .withId(CONTACT_DETAILS_ID)
                .withPersonId(PERSON_ID)
                .withPersonDTO(aPersonDTO()
                        .withId(PERSON_ID)
                        .withName(SOME_PERSON_NAME)
                        .build())
                .withTelephoneNumber(SOME_TELEPHONE_NUMBER)
                .withCellphoneNumber(SOME_CELLPHONE_NUMBER)
                .withPostalAddress(SOME_POSTAL_ADDRESS)
                .withHomeAddress(SOME_HOME_ADDRESS)
                .build();

        // Exercise SUT
        ContactDetailsDTO returnedContactDetailsDTO = contactDetailsDtoFactory.build(contactDetails, person);

        // Verify behaviour
        assertThat(returnedContactDetailsDTO, sameBeanAs(expectedContactDetails));
    }

    /**
     * Show argument matchers.
     * Show shazamcrest .ignore()
     */
    @Test
    public void testBuildWithPersonMock() {
        // Set up fixture
        PersonDtoFactory mockPersonDtoFactory = mock(PersonDtoFactory.class);
        contactDetailsDtoFactory = new ContactDetailsDtoFactory(mockPersonDtoFactory);
        Person person = aPerson()
                .withId(PERSON_ID)
                .withName(SOME_PERSON_NAME)
                .build();
        ContactDetails contactDetails = aContactDetails()
                .withId(CONTACT_DETAILS_ID)
                .withPersonId(PERSON_ID)
                .withTelephoneNumber(SOME_TELEPHONE_NUMBER)
                .withCellphoneNumber(SOME_CELLPHONE_NUMBER)
                .withPostalAddress(SOME_POSTAL_ADDRESS)
                .withHomeAddress(SOME_HOME_ADDRESS)
                .build();
        ContactDetailsDTO expectedContactDetails = aContactDetailsDTO()
                .withId(CONTACT_DETAILS_ID)
                .withPersonId(PERSON_ID)
                .withTelephoneNumber(SOME_TELEPHONE_NUMBER)
                .withCellphoneNumber(SOME_CELLPHONE_NUMBER)
                .withPostalAddress(SOME_POSTAL_ADDRESS)
                .withHomeAddress(SOME_HOME_ADDRESS)
                .build();

        // Set expectations
        when(mockPersonDtoFactory.build(any(Person.class))).thenReturn(aPersonDTO().build());

        // Exercise SUT
        ContactDetailsDTO returnedContactDetailsDTO = contactDetailsDtoFactory.build(contactDetails, person);

        // Verify behaviour
        verify(mockPersonDtoFactory).build(same(person));
        assertThat(returnedContactDetailsDTO, is(sameBeanAs(expectedContactDetails).ignoring(PersonDTO.class)));
    }
}
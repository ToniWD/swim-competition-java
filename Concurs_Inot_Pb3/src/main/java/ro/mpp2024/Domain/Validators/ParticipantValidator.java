package ro.mpp2024.Domain.Validators;

import ro.mpp2024.Domain.Participant;

public class ParticipantValidator implements Validator<Participant> {

    private boolean validateName(String name){
        return !(name == null || name.isEmpty() || !name.matches("[a-zA-Z ]+"));
    }

    @Override
    public void validate(Participant participant) throws ValidatorException {
        StringBuilder builder = new StringBuilder();

        if(!validateName(participant.getFirstName()))builder.append("\nInvalid first name");
        if(!validateName(participant.getLastName()))builder.append("\nInvalid last name");
        if(participant.getAge() < 0)builder.append("\nInvalid age");

        if(!builder.isEmpty()) throw new ValidatorException(builder.toString());
    }
}

package gz.test.mostgreat2.common.register.model;

import java.io.Serializable;

public class RegisterCredentials implements Serializable {

	private static final long serialVersionUID = 6182390898687671093L;
	
	private String InputName;
	private String InputId;
	private String InputPassWd;
	private String confirmPassWd;
	private int InputAge;
	private String InputGender;
	private String InputCity;
	public String getInputName() {
		return InputName;
	}
	public void setInputName(String inputName) {
		InputName = inputName;
	}
	public String getInputId() {
		return InputId;
	}
	public void setInputId(String inputId) {
		InputId = inputId;
	}
	public String getInputPassWd() {
		return InputPassWd;
	}
	public void setInputPassWd(String inputPassWd) {
		InputPassWd = inputPassWd;
	}
	public String getConfirmPassWd() {
		return confirmPassWd;
	}
	public void setConfirmPassWd(String confirmPassWd) {
		this.confirmPassWd = confirmPassWd;
	}
	public int getInputAge() {
		return InputAge;
	}
	public void setInputAge(int inputAge) {
		InputAge = inputAge;
	}
	public String getInputGender() {
		return InputGender;
	}
	public void setInputGender(String inputGender) {
		InputGender = inputGender;
	}
	public String getInputCity() {
		return InputCity;
	}
	public void setInputCity(String inputCity) {
		InputCity = inputCity;
	}
	
	
	
}

package shp.template.Data.ResponseDataJson.getMasterDokter;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class DataDokterItem{

	@SerializedName("firstname")
	private String firstname;

	@SerializedName("txtKecId")
	private String txtKecId;

	@SerializedName("gender")
	private String gender;

	@SerializedName("specialist")
	private String specialist;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	@SerializedName("txtKecName")
	private String txtKecName;

	@SerializedName("lastname")
	private String lastname;

	public void setFirstname(String firstname){
		this.firstname = firstname;
	}

	public String getFirstname(){
		return firstname;
	}

	public void setTxtKecId(String txtKecId){
		this.txtKecId = txtKecId;
	}

	public String getTxtKecId(){
		return txtKecId;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setSpecialist(String specialist){
		this.specialist = specialist;
	}

	public String getSpecialist(){
		return specialist;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setTxtKecName(String txtKecName){
		this.txtKecName = txtKecName;
	}

	public String getTxtKecName(){
		return txtKecName;
	}

	public void setLastname(String lastname){
		this.lastname = lastname;
	}

	public String getLastname(){
		return lastname;
	}

	@Override
 	public String toString(){
		return 
			"DataDokterItem{" + 
			"firstname = '" + firstname + '\'' + 
			",txtKecId = '" + txtKecId + '\'' + 
			",gender = '" + gender + '\'' + 
			",specialist = '" + specialist + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			",txtKecName = '" + txtKecName + '\'' + 
			",lastname = '" + lastname + '\'' + 
			"}";
		}
}
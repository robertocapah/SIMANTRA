package shp.template.ResponseDataJson.downloadtAkuisisi;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class NotificationDataItem{

	@SerializedName("txtDokterId")
	private String txtDokterId;

	@SerializedName("txtAkuisisiHeaderId")
	private String txtAkuisisiHeaderId;

	@SerializedName("txtNoDoc")
	private String txtNoDoc;

	@SerializedName("txtApotekId")
	private String txtApotekId;

	@SerializedName("intActivityId")
	private int intActivityId;

	@SerializedName("dtExpiredDate")
	private String dtExpiredDate;

	@SerializedName("intSubDetailActivityId")
	private int intSubDetailActivityId;

	public void setTxtDokterId(String txtDokterId){
		this.txtDokterId = txtDokterId;
	}

	public String getTxtDokterId(){
		return txtDokterId;
	}

	public void setTxtAkuisisiHeaderId(String txtAkuisisiHeaderId){
		this.txtAkuisisiHeaderId = txtAkuisisiHeaderId;
	}

	public String getTxtAkuisisiHeaderId(){
		return txtAkuisisiHeaderId;
	}

	public void setTxtNoDoc(String txtNoDoc){
		this.txtNoDoc = txtNoDoc;
	}

	public String getTxtNoDoc(){
		return txtNoDoc;
	}

	public void setTxtApotekId(String txtApotekId){
		this.txtApotekId = txtApotekId;
	}

	public String getTxtApotekId(){
		return txtApotekId;
	}

	public void setIntActivityId(int intActivityId){
		this.intActivityId = intActivityId;
	}

	public int getIntActivityId(){
		return intActivityId;
	}

	public void setDtExpiredDate(String dtExpiredDate){
		this.dtExpiredDate = dtExpiredDate;
	}

	public String getDtExpiredDate(){
		return dtExpiredDate;
	}

	public void setIntSubDetailActivityId(int intSubDetailActivityId){
		this.intSubDetailActivityId = intSubDetailActivityId;
	}

	public int getIntSubDetailActivityId(){
		return intSubDetailActivityId;
	}

	@Override
 	public String toString(){
		return 
			"NotificationDataItem{" + 
			"txtDokterId = '" + txtDokterId + '\'' + 
			",txtAkuisisiHeaderId = '" + txtAkuisisiHeaderId + '\'' + 
			",txtNoDoc = '" + txtNoDoc + '\'' + 
			",txtApotekId = '" + txtApotekId + '\'' + 
			",intActivityId = '" + intActivityId + '\'' + 
			",dtExpiredDate = '" + dtExpiredDate + '\'' + 
			",intSubDetailActivityId = '" + intSubDetailActivityId + '\'' + 
			"}";
		}
}
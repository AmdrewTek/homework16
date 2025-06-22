package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserListResponseModel {
  private int page;
  @JsonProperty("per_page")
  private int perPage;
  private int total;
  @JsonProperty("total_pages")
  private int totalPages;
  private List<UserDataModel> data;
}

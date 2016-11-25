//package edu.sjsu.LPOS.domain;
//
//import java.util.List;
//
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.ManyToMany;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//
//@Entity
//@Table(name = "AUTHORITY")
//public class Authority {
//
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	@Id
//	private long authorityId;
//	
//    @NotNull
//    @Enumerated(EnumType.STRING)
//	private AuthorityName name;
//	
////    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
//	private List<User> users;
//
//	public long getId() {
//		return authorityId;
//	}
//
//	public void setId(long id) {
//		this.authorityId = id;
//	}
//
//	public AuthorityName getName() {
//		return name;
//	}
//
//	public void setName(AuthorityName name) {
//		this.name = name;
//	}
//
//	public List<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<User> users) {
//		this.users = users;
//	}
//}

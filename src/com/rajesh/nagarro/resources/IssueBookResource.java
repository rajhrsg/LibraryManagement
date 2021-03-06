package com.rajesh.nagarro.resources;

import java.util.List;
import com.rajesh.nagarro.model.User;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import com.rajesh.nagarro.constants.GlobalConstants;
import com.rajesh.nagarro.dao.UserDao;
import com.rajesh.nagarro.model.IssuedBook;
import com.rajesh.nagarro.model.common.Message;
import org.springframework.beans.factory.annotation.Autowired;


@Path("issue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IssueBookResource {
	@Autowired
	UserDao userDao;

	private Logger logger = Logger.getLogger(IssueBookResource.class);

	/**
	 * @param issuedBook
	 * @return
	 */
	@POST
	public Response issueBook(IssuedBook issuedBook) {
		Message message = null;
		logger.info("Book category message " + issuedBook);

		boolean result = userDao.isBookIssued(issuedBook.getBookId(), issuedBook.getUserId());
		if (!result) {
			result = userDao.issueBook(issuedBook);
			if (result) {
				message = new Message(true, GlobalConstants.ADD_SUCCESS, null);
			} else {
				message = new Message(false, GlobalConstants.ADD_FAULURE, null);
			}
		} else {
			message = new Message(false, "Book Already issued to this student.", null);
		}
		return Response.status(Status.OK).entity(message).build();
	}

	/**
	 * @param userId
	 * @return
	 */
	@GET
	@Path("{id}")
	public Response getIssuedBooksByUserId(@PathParam("id") int userId) {
		Message message = null;
		User user = new User();
		System.out.print(user.getUserId());
		List<IssuedBook> object = userDao.getIssuedBooksByUserId(userId);
		if (object == null) {
			message = new Message(false, GlobalConstants.NO_RECORD, null);
		} else {
			message = new Message(true, null, object);
		}
		return Response.status(Status.OK).entity(message).build();
	}

	/**
	 * @return
	 */
	@GET
	public Response getIssuedBooks() {
		Message message = null;
		List<IssuedBook> object = userDao.getIssuedBooks();
		if (object == null) {
			message = new Message(false, GlobalConstants.NO_RECORD, null);
		} else {
			message = new Message(true, null, object);
		}
		return Response.status(Status.OK).entity(message).build();
	}
}
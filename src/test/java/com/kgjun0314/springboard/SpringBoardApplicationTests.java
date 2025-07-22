package com.kgjun0314.springboard;

import com.kgjun0314.springboard.application.PostService;
import com.kgjun0314.springboard.application.UserService;
import com.kgjun0314.springboard.domain.entity.Comment;
import com.kgjun0314.springboard.domain.entity.Post;
import com.kgjun0314.springboard.domain.entity.SiteUser;
import com.kgjun0314.springboard.domain.repository.PostRepository;
import com.kgjun0314.springboard.infrastructure.JpaCommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class SpringBoardApplicationTests {
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private JpaCommentRepository commentRepository;
	@Autowired
	private PostService postService;
    @Autowired
    private UserService userService;

	@Test
	@DisplayName("JPA findById")
	void testJpa2() {
		// given
		Post p1 = new Post();
		p1.setTitle("title1");
		p1.setContent("content1");
		p1.setCreatedDate(LocalDateTime.now());

		Post p2 = new Post();
		p2.setTitle("title2");
		p2.setContent("content2");
		p2.setCreatedDate(LocalDateTime.now());

		// when
		this.postRepository.save(p1);
		this.postRepository.save(p2);

		Optional<Post> p3 = this.postRepository.findById(p1.getId());

		// then
		if (p3.isPresent()) {
			Post post = p3.get();
			assertEquals("title1", post.getTitle());
		}
	}

	@Test
	@DisplayName("JPA edit")
	void testJpa6() {
		// given
		Post p1 = new Post();
		p1.setTitle("title1");
		p1.setContent("content1");
		p1.setCreatedDate(LocalDateTime.now());

		Post p2 = new Post();
		p2.setTitle("title2");
		p2.setContent("content2");
		p2.setCreatedDate(LocalDateTime.now());

		// when
		this.postRepository.save(p1);
		this.postRepository.save(p2);

		Optional<Post> p3 = this.postRepository.findById(p1.getId());

		// then
		assertTrue(p3.isPresent());
		Post p4 = p3.get();
		p4.setTitle("title3");
		this.postRepository.save(p4);
	}

	@Test
	@DisplayName("Comment Creation")
	void testJpa7() {
		// given
		Post p1 = new Post();
		p1.setTitle("title1");
		p1.setContent("content1");
		p1.setCreatedDate(LocalDateTime.now());

		Comment c1 = new Comment();
		c1.setContent("content2");
		c1.setPost(p1);
		c1.setCreatedDate(LocalDateTime.now());

		// when
		this.postRepository.save(p1);
		this.commentRepository.save(c1);
		Optional<Comment> oc = this.commentRepository.findById(c1.getId());

		// then
		assertTrue(oc.isPresent());
		Comment c = oc.get();
		assertEquals(p1.getId(), c.getPost().getId());
	}

	@Transactional
	@Test
	@DisplayName("Comment Creation")
	void testJpa8() {
		// given
		Post p1 = new Post();
		p1.setTitle("title1");
		p1.setContent("content1");
		p1.setCreatedDate(LocalDateTime.now());

		Comment c1 = new Comment();
		c1.setContent("content2");
		c1.setPost(p1);
		c1.setCreatedDate(LocalDateTime.now());

		// when
		this.postRepository.save(p1);
		this.commentRepository.save(c1);
		Optional<Post> op = this.postRepository.findById(p1.getId());

		// then
		assertTrue(op.isPresent());
		Post p = op.get();
		List<Comment> commentList = p.getCommentList();
		commentList.add(c1);
		assertEquals(1, commentList.size());
		assertEquals("content2", commentList.get(0).getContent());
	}

	@Test
	@DisplayName("input 300 data")
	void input() {
		userService.create("user", "mail@mail.com", "password");
		SiteUser siteUser = userService.getUser("user");
		for(int i = 1; i <= 300; i++) {
			String title = String.format("test title:[%03d]", i);
			String content = "test content";
			postService.create(title, content, siteUser);
		}
	}
}

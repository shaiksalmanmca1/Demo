import React, { useState, useEffect } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import {
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  CardActions,
  Button,
  Pagination,
  Box,
} from '@mui/material';
import axios from 'axios';

const RecentArticles = () => {
  const [articles, setArticles] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchRecentArticles = async () => {
    try {
      setLoading(true);
      const response = await axios.get(`/api/articles/recent?page=${page - 1}&size=10`);
      setArticles(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      setError('Failed to fetch recently viewed articles');
      console.error('Error fetching recently viewed articles:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRecentArticles();
  }, [page]);

  const handlePageChange = (event, value) => {
    setPage(value);
  };

  if (loading) {
    return (
      <Container>
        <Typography>Loading...</Typography>
      </Container>
    );
  }

  if (error) {
    return (
      <Container>
        <Typography color="error">{error}</Typography>
      </Container>
    );
  }

  return (
    <Container>
      <Typography variant="h4" component="h1" gutterBottom>
        Recently Viewed Articles
      </Typography>

      {articles.length === 0 ? (
        <Typography>No recently viewed articles</Typography>
      ) : (
        <>
          <Grid container spacing={4}>
            {articles.map((article) => (
              <Grid item xs={12} sm={6} md={4} key={article.id}>
                <Card>
                  <CardContent>
                    <Typography variant="h6" component="h2" gutterBottom>
                      {article.title}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" gutterBottom>
                      By {article.authorUsername}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {new Date(article.createdAt).toLocaleDateString()}
                    </Typography>
                  </CardContent>
                  <CardActions>
                    <Button
                      size="small"
                      color="primary"
                      component={RouterLink}
                      to={`/articles/${article.id}`}
                    >
                      Read More
                    </Button>
                  </CardActions>
                </Card>
              </Grid>
            ))}
          </Grid>

          {totalPages > 1 && (
            <Box sx={{ mt: 4, display: 'flex', justifyContent: 'center' }}>
              <Pagination
                count={totalPages}
                page={page}
                onChange={handlePageChange}
                color="primary"
              />
            </Box>
          )}
        </>
      )}
    </Container>
  );
};

export default RecentArticles; 
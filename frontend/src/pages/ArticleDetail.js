import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link as RouterLink } from 'react-router-dom';
import {
  Container,
  Typography,
  Box,
  Button,
  Paper,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Alert,
} from '@mui/material';
import axios from 'axios';
import { useAuth } from '../contexts/AuthContext';

const ArticleDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [article, setArticle] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);

  const fetchArticle = async () => {
    try {
      setLoading(true);
      const response = await axios.get(`/api/articles/${id}`);
      setArticle(response.data);
    } catch (error) {
      setError('Failed to fetch article');
      console.error('Error fetching article:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchArticle();
  }, [id]);

  const handleDelete = async () => {
    try {
      await axios.delete(`/api/articles/${id}`);
      navigate('/');
    } catch (error) {
      setError('Failed to delete article');
      console.error('Error deleting article:', error);
    }
    setDeleteDialogOpen(false);
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

  if (!article) {
    return (
      <Container>
        <Typography>Article not found</Typography>
      </Container>
    );
  }

  const isAuthor = user?.username === article.authorUsername;

  return (
    <Container maxWidth="md">
      <Paper 
        elevation={0}
        sx={{ 
          mt: 4,
          mb: 4,
          borderRadius: 3,
          overflow: 'hidden',
          border: '1px solid rgba(0, 0, 0, 0.12)',
          boxShadow: '0 2px 12px rgba(0, 0, 0, 0.05)',
        }}
      >
        {/* Header Section with grey background */}
        <Box
          sx={{
            backgroundColor: '#f8f9fa',
            p: { xs: 3, md: 4 },
            borderBottom: '1px solid rgba(0, 0, 0, 0.08)',
          }}
        >
          <Typography 
            variant="h3" 
            component="h1" 
            gutterBottom
            sx={{
              fontWeight: 800,
              mb: 3,
              background: 'linear-gradient(45deg, #1976d2, #1565c0)',
              backgroundClip: 'text',
              WebkitBackgroundClip: 'text',
              color: 'transparent',
              lineHeight: 1.3,
            }}
          >
            {article.title}
          </Typography>
          
          <Box sx={{ 
            display: 'flex',
            gap: 2,
            alignItems: 'center',
            flexWrap: 'wrap'
          }}>
            <Typography 
              variant="body1" 
              sx={{ 
                color: 'primary.main',
                fontWeight: 500,
                display: 'flex',
                alignItems: 'center',
                gap: 1,
              }}
            >
              By {article.authorUsername}
            </Typography>
            <Typography 
              variant="body2" 
              sx={{ 
                color: 'text.secondary',
                fontStyle: 'italic',
                display: 'flex',
                alignItems: 'center',
                '&::before': {
                  content: '"•"',
                  marginRight: '8px',
                }
              }}
            >
              Published on {new Date(article.createdAt).toLocaleDateString()}
            </Typography>
          </Box>
        </Box>

        {/* Content Section with white background */}
        <Box
          sx={{
            backgroundColor: '#ffffff',
            p: { xs: 3, md: 4 },
          }}
        >
          <Typography 
            variant="body1" 
            paragraph
            sx={{
              fontSize: '1.1rem',
              lineHeight: 1.8,
              color: 'text.primary',
              mb: 6,
            }}
          >
            {article.content}
          </Typography>
        </Box>

        {/* Actions Section */}
        <Box sx={{ 
          p: { xs: 3, md: 4 },
          pt: 3,
          borderTop: 1,
          borderColor: 'divider',
          display: 'flex', 
          gap: 2,
          flexWrap: 'wrap',
          backgroundColor: '#ffffff',
        }}>
          <Button
            variant="outlined"
            color="primary"
            component={RouterLink}
            to="/"
            sx={{
              borderRadius: 2,
              textTransform: 'none',
              px: 3,
              py: 1,
              fontSize: '0.95rem',
              '&:hover': {
                backgroundColor: 'rgba(25, 118, 210, 0.04)',
              },
            }}
          >
            Back to Articles
          </Button>
          {isAuthor && (
            <>
              <Button
                variant="contained"
                color="primary"
                component={RouterLink}
                to={`/articles/${id}/edit`}
                sx={{
                  borderRadius: 2,
                  textTransform: 'none',
                  px: 3,
                  py: 1,
                  fontSize: '0.95rem',
                  boxShadow: '0 2px 8px rgba(25, 118, 210, 0.25)',
                  '&:hover': {
                    boxShadow: '0 4px 12px rgba(25, 118, 210, 0.35)',
                  },
                }}
              >
                Edit Article
              </Button>
              <Button
                variant="contained"
                color="error"
                onClick={() => setDeleteDialogOpen(true)}
                sx={{
                  borderRadius: 2,
                  textTransform: 'none',
                  px: 3,
                  py: 1,
                  fontSize: '0.95rem',
                  boxShadow: '0 2px 8px rgba(211, 47, 47, 0.25)',
                  '&:hover': {
                    boxShadow: '0 4px 12px rgba(211, 47, 47, 0.35)',
                  },
                }}
              >
                Delete Article
              </Button>
            </>
          )}
        </Box>
      </Paper>

      <Dialog
        open={deleteDialogOpen}
        onClose={() => setDeleteDialogOpen(false)}
      >
        <DialogTitle>Delete Article</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete this article? This action cannot be undone.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleDelete} color="error">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default ArticleDetail;
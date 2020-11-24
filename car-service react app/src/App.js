import React from 'react';
//import './App.css';
import Button from '@material-ui/core/Button';
import { AppBar, Container, IconButton, Toolbar, Typography, Box, Paper, Grid, 
  Card, CardContent, CardMedia, CardActions} from '@material-ui/core';

  import BottomNavigationAction from '@material-ui/core/BottomNavigationAction'
  import BottomNavigation from '@material-ui/core/BottomNavigation'

  import FolderIcon from "@material-ui//icons/Folder"
  import RestoreIcon from "@material-ui//icons/Restore"
  import FavoriteIcon from "@material-ui//icons/Favorite"
  import LocationOnIcon from '@material-ui/icons/LocationOn'

import MenuIcon from '@material-ui/icons/Menu';
import LayerIcon from '@material-ui/icons/Layers';
import PlayedCircledFilledIcon from '@material-ui/icons/PlayCircleFilled';
import {makeStyles} from '@material-ui/core/styles';

import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from "@material-ui/core/DialogTitle";




const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1
  },
  menuButton: {
    marginRight: theme.spacing(1)
  },
  title: {
    flexGrow: 1
  },
  mainFeaturesPost : {
    position: "relative",
    color: theme.palette.common.white,
    marginBottom: theme.spacing(4),

    backgroundSize: "cover",
    backgroundRepeat: "no-repeat",
    backgroundPosition: "center"
  },

  overlay : {
    position: "absolute",
    top: 0,
    bottom: 0,
    right: 0,
    left: 0,
    backgroundOverlay: "rgba(0,0,0,.3)"
  },

  mainFeaturesPostContent: {
    position: "relative",
    padding: theme.spacing(15),
    marginTop: theme.spacing(8),
  },

  CardMedia: {
    paddingTop: "56.25%"
  },

  cardContent: {
    flexGrow: 1

  },

  cardGrid: {
    marginTop: theme.spacing(4)
  }

}))

const cards = [1,2,3,4,5,6,7,8,9];

function App() {
  const classes = useStyles();
  const [value, setValue] = React.useState("recents")

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };
  
const [open, setOpen] = React.useState(false);

const handleClickOpen = () => {
  setOpen(true);
}

const handleClose = () => {
  setOpen(false);
}


  return (
    <>
    <AppBar position="fixed">
      <Container fixed>
        <Toolbar>
          <IconButton edge="start" color="inherit" aria-laabel="menu" className={classes.menuButton}> 
            <MenuIcon></MenuIcon>
          </IconButton>
          <Typography 
          variant="h6"  
          className={classes.title}>Car Rental Service</Typography>
          <Box mr={3} ml={3}>
            <Button color="inherit" variant="outlined" onClick={handleClickOpen}>Log in</Button>
            <Dialog
            open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Log in</DialogTitle>
                <DialogContent>
                  <DialogContentText>
                    Log in to make an order
                  </DialogContentText>
                  <TextField autoFocus
                  margin="dense"
                  id="name"
                  label="Email address"
                  type="email"
                  fullWidth>
                  </TextField>

                  <TextField autoFocus
                  margin="dense"
                  id="name"
                  label="Password"
                  type="password"
                  fullWidth>
                  </TextField>
                </DialogContent>
                <DialogActions>

                  <Button 
                  onClick={handleClose} color="primary">
                    Cancel
                  </Button>
                  <Button 
                  onClick={handleClose} color="primary">
                    Log in
                  </Button>
                  
                </DialogActions>
            </Dialog>
          </Box>
          <Button color="secondary" variant="contained">Sign up</Button>
        </Toolbar>
      </Container>
    </AppBar>

    <main>
      <Paper className={classes.mainFeaturesPost} style={{backgroundImage: 'url(https://images.unsplash.com/photo-1594462695680-b2b1a5b0aa15?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80)'}}>
        <Container fixed>
          <div className={classes.overlay}>

          </div>
            <Grid container>
              <Grid item md={10}>
                <div className = {classes.mainFeaturesPostContent}>
                  <Typography
                  component="h1"
                  variant="h2"
                  color="inherit"
                  gutterBottom>
                    Start your journey
                  </Typography>

                  <Typography
                  variant="h3"
                  color="inherit"
                  paragraph>
                    with the best car rental service in Szczecin
                  </Typography>

                  <Button variant="contained"
                  color='secondary'>
                    Order a car now!
                  </Button>
                </div>
              </Grid>
            </Grid>
        </Container>
      </Paper>
      <div className={classes.mainContent}>
        <Container maxWidth="md">
          <Typography variant="h2" align="center" color="textPrimary" gutterBotom>
            Car Rental Service
          </Typography>
          <Typography variant="h5" align="center" color="textSecondary" paragraph>
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do 
          eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim 
          ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut 
          aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit 
          in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur 
          sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt 
          mollit anim id est laborum."
          </Typography>

          <div className={classes.mainButons}>
            <Grid container spacing={5} justify="center">
                <Grid item>
                  <Button variant="contained" color="primary">
                    Start now
                  </Button>
                  </Grid>
                  <Grid item>
                  <Button variant="outlined" color="primary">
                    Find out more
                  </Button>
                </Grid>
            </Grid>
          </div>
        </Container>
      </div>

      <Container className={classes.cardGrid} maxWidth="md">
          <Grid container spacing={4}>
            {cards.map((card) => 
            <Grid item key={card} xs={12} sm={6} md={4}>
                <Card className={classes.card}>
                  <CardMedia
                    className={classes.CardMedia}
                    image="https://cdn.pixabay.com/photo/2014/06/14/05/54/car-368636_1280.jpg"
                    title="image title">
                    </CardMedia>
                    <CardContent
                    className={classes.cardContent}>
                      <Typography variant="h5" gutterBottom> 
                          blogpost
                      </Typography>
                      <Typography variant="h5" gutterBottom> 
                          blogpost description
                      </Typography>
                    </CardContent>
                    <CardActions>
                      <Button size="small" color="primary">
                        view
                      </Button>
                      <Button size="small" color="primary">
                        edit
                      </Button>

                      <LayerIcon>
                      </LayerIcon>
                      <PlayedCircledFilledIcon>
                      </PlayedCircledFilledIcon>
                    </CardActions>
                </Card>
              </Grid>
              )}
          </Grid>
      </Container>
    </main>
    <footer>
              <Typography variant="h6" align="center" gutterBottom>
                Footer
              </Typography>
              <BottomNavigation
              value={value}
              onChange={handleChange}
              className={classes.root}>
                  <BottomNavigationAction
                  label="Recents"
                  value="recents"
                  icon={<RestoreIcon> </RestoreIcon>}>
                  </BottomNavigationAction>

                  <BottomNavigationAction
                  label="Favorites"
                  value="favorites"
                  icon={<FavoriteIcon> </FavoriteIcon>}>
                  </BottomNavigationAction>

                  <BottomNavigationAction
                  label="Nearby"
                  value="nearby"
                  icon={<LocationOnIcon> </LocationOnIcon>}>
                  </BottomNavigationAction>

                  <BottomNavigationAction
                  label="Folder"
                  value="folder"
                  icon={<FolderIcon> </FolderIcon>}>
                  </BottomNavigationAction>
              </BottomNavigation>
              <Typography align="center"
               color="textSecondary" component="p"
               variant="subtitle1">
                Car rental service in Stettin. The best car rental service in the West Pomeranian Voivodeship
              </Typography>
    </footer>
    </>
  );
}

export default App;

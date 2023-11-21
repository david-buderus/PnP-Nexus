import { Universe, UniverseServiceApi } from '../api';
import { Outlet, useOutletContext } from 'react-router-dom';
import { Component } from 'react';
import { FaUser } from "react-icons/fa";
import { Autocomplete, TextField } from '@mui/material';
import MainSidebar from './MainSidebar';

type ContextType = { universes: Universe[] | [], activeUniverse: Universe | null };


class PageBase extends Component {

  constructor(props) {
    super(props);
    this.state = {
      universes: [],
      activeUniverse: null
    };
  }

  componentDidMount() {
    const universeApi = new UniverseServiceApi();

    universeApi.getAllUniverses().then(response => {
      const universes = response.data;
      if (this.state.activeUniverse === null && universes.length > 0) {
        this.setState({ activeUniverse: universes[0] });
      }
      this.setState({ universes: universes });
    })
  }

  render() {
    const universes = this.state.universes;

    return (
      <div className='flex h-full'>
        <MainSidebar universes={universes}></MainSidebar>
        <div className='w-full'>
          <header className="flow-root bg-stone-200 p-1">
            {universes.length > 0 ?
              <Autocomplete
                className="float-left w-60 h-11"
                options={universes}
                getOptionLabel={(option: Universe) => {
                  return option?.displayName;
                }}
                renderInput={(params) => <TextField {...params} className='h-10' variant='standard' size="small" label="Universe" />}
                onChange={(_, value) => {
                  this.setState({ activeUniverse: value });
                }}
              />
              : ''}
            <div className=' float-right'>
              <button className='text-center inline-flex items-center btn-icon h-9 w-9'>
                <FaUser />
              </button>
            </div>
          </header>
          <div className='p-2'>
            <Outlet context={{ universes: this.state.universes, activeUniverse: this.state.activeUniverse }} />
          </div>
        </div>
      </div>
    );
  }
}

export function getUniverseContext() {
  return useOutletContext<ContextType>();
}

export default PageBase;

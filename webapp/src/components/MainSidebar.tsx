import React, { useState } from "react";
import { Sidebar,  Menu, MenuItem, SubMenu } from 'react-pro-sidebar';

import { FiArrowLeftCircle, FiArrowRightCircle } from "react-icons/fi";
import { TfiWorld } from "react-icons/tfi";
import { GiDiceSixFacesFour, GiSwapBag, GiAxeSword, GiShield, GiChestArmor, GiRing } from "react-icons/gi";
import { Link } from "react-router-dom";
import { UserPermissions } from "./interfaces/UserPermissions";

interface MainSidebarProps {
  userPermissions: UserPermissions;
}

function MainSidebar(props: React.PropsWithChildren<MainSidebarProps>) {

  const [menuCollapse, setMenuCollapse] = useState(false);

  const menuIconClick = () => {
    setMenuCollapse(!menuCollapse);
  };

  return (
    <Sidebar collapsed={menuCollapse} backgroundColor="rgb(231 229 228)" className=" border-none">
      <header className="p-3">
        <div className="text-3xl font-bold">
          <p>{menuCollapse ? <GiDiceSixFacesFour/> : "P&P Nexus"}</p>
        </div>
        <div className="right-2 top-4 cursor-pointer z-50 absolute font-bold text-xl" onClick={menuIconClick}>
          {menuCollapse ? <FiArrowRightCircle/> : <FiArrowLeftCircle/>}
        </div>
      </header>
      <Menu>
        <MenuItem icon={<TfiWorld/>} component={<Link to="/universe" />}> Universe </MenuItem>
        <SubMenu label="Items" icon={<GiSwapBag/>} component={<Link to ="/items" />}>
          <MenuItem icon={<GiAxeSword/>} component={<Link to="/weapons" />}> Weapons </MenuItem>
          <MenuItem icon={<GiShield/>} component={<Link to="/shields" />}> Shields </MenuItem>
          <MenuItem icon={<GiChestArmor/>} component={<Link to="/armor" />}> Armor </MenuItem>
          <MenuItem icon={<GiRing/>} component={<Link to="/jewellery" />}> Jewellery </MenuItem>
        </SubMenu>
        { props.userPermissions.isAdmin ? <MenuItem component={<Link to="/" />}> Home </MenuItem> : "" }
        { props.userPermissions.isAdmin ? <MenuItem component={<Link to="/about" />}> About </MenuItem> : "" }
      </Menu>
    </Sidebar>
  );
}

export default  MainSidebar;

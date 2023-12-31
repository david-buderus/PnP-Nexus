import React, { useState } from "react";
import { Sidebar,  Menu, MenuItem, SubMenu } from 'react-pro-sidebar';

import { FiArrowLeftCircle, FiArrowRightCircle } from "react-icons/fi";
import { TfiWorld } from "react-icons/tfi";
import { GiDiceSixFacesFour, GiSwapBag, GiAxeSword, GiShield, GiChestArmor, GiRing } from "react-icons/gi";
import { Link, To, useSearchParams } from "react-router-dom";
import { UserPermissions } from "./interfaces/UserPermissions";

interface MainSidebarProps {
  userPermissions: UserPermissions;
}

function buildLink(path: string, searchParams: URLSearchParams): To {
  return {
    pathname: path, 
    search: searchParams.toString()
  }
}

function MainSidebar(props: React.PropsWithChildren<MainSidebarProps>) {

  const [menuCollapse, setMenuCollapse] = useState(false);
  const [searchParams, _] = useSearchParams();

  const menuIconClick = () => {
    setMenuCollapse(!menuCollapse);
  };

  return (
    <Sidebar collapsed={menuCollapse} backgroundColor="rgb(231 229 228)" className="border-none h-full">
      <header className="p-3">
        <div className="text-3xl font-bold">
          <p>{menuCollapse ? <GiDiceSixFacesFour/> : "P&P Nexus"}</p>
        </div>
        <div className="right-2 top-4 cursor-pointer z-50 absolute font-bold text-xl" onClick={menuIconClick}>
          {menuCollapse ? <FiArrowRightCircle/> : <FiArrowLeftCircle/>}
        </div>
      </header>
      <Menu>
        <MenuItem icon={<TfiWorld/>} component={<Link to={buildLink("/universe", searchParams)} />}> Universe </MenuItem>
        <SubMenu label="Items" icon={<GiSwapBag/>} component={<Link to ={buildLink("/items", searchParams)} />}>
          <MenuItem icon={<GiAxeSword/>} component={<Link to={buildLink("/weapons", searchParams)} />}> Weapons </MenuItem>
          <MenuItem icon={<GiShield/>} component={<Link to={buildLink("/shields", searchParams)} />}> Shields </MenuItem>
          <MenuItem icon={<GiChestArmor/>} component={<Link to={buildLink("/armor", searchParams)} />}> Armor </MenuItem>
          <MenuItem icon={<GiRing/>} component={<Link to={buildLink("/jewellery", searchParams)} />}> Jewellery </MenuItem>
        </SubMenu>
        { props.userPermissions.isAdmin ? <MenuItem component={<Link to={buildLink("/", searchParams)} />}> Home </MenuItem> : "" }
        { props.userPermissions.isAdmin ? <MenuItem component={<Link to={buildLink("/about", searchParams)} />}> About </MenuItem> : "" }
      </Menu>
    </Sidebar>
  );
}

export default  MainSidebar;

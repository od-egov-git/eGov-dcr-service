package org.egov.edcr.od;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.entity.edcr.AccessoryBlock;
import org.egov.common.entity.edcr.Ammenity;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Floor;
import org.egov.common.entity.edcr.FloorUnit;
import org.egov.common.entity.edcr.Measurement;
import org.egov.common.entity.edcr.MeasurementWithHeight;
import org.egov.common.entity.edcr.Occupancy;
import org.egov.common.entity.edcr.OccupancyTypeHelper;
import org.egov.common.entity.edcr.ParkingDetails;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Room;
import org.egov.common.entity.edcr.RoomHeight;
import org.egov.edcr.constants.DxfFileConstants;
import org.egov.edcr.feature.Parking;

public class OdishaUtill {

	private static final BigDecimal MINIMUM_NUMBER_OF_OCCUPANTS_OR_USERS_FOR_ASSEMBLY_BUILDING = new BigDecimal("50");
	private static final int COLOR_EWS = 1;
	private static final int COLOR_LIG = 2;
	private static final int COLOR_MIG1 = 3;
	private static final int COLOR_MIG2 = 4;
	private static final int COLOR_OTHER = 5;
	private static final int COLOR_ROOM = 6;
	private static final int COLOR_OWNERS_SOCIETY_OFFICE = 8;

	public static boolean isAssemblyBuildingCriteria(Plan pl) {
		boolean isAssemblyBuilding = false;
		OccupancyTypeHelper occupancyTypeHelper = pl.getVirtualBuilding().getMostRestrictiveFarHelper();

		if (DxfFileConstants.OC_PUBLIC_SEMI_PUBLIC_OR_INSTITUTIONAL.equals(occupancyTypeHelper.getType().getCode())
				|| DxfFileConstants.OC_COMMERCIAL.equals(occupancyTypeHelper.getType().getCode())
				|| DxfFileConstants.OC_TRANSPORTATION.equals(occupancyTypeHelper.getType().getCode())) {

			// BigDecimal providedNumberOfOccupantsOrUser =
			// pl.getPlanInformation().getNumberOfOccupantsOrUsers();
			BigDecimal providedNumberOfOccupantsOrUser = BigDecimal.ZERO;
			for (Block block : pl.getBlocks()) {
				if (block.getNumberOfOccupantsOrUsersOrBedBlk() != null) {
					isAssemblyBuildingCriteria(pl, block);
					providedNumberOfOccupantsOrUser = providedNumberOfOccupantsOrUser
							.add(block.getNumberOfOccupantsOrUsersOrBedBlk());

				}
			}

			if (providedNumberOfOccupantsOrUser
					.compareTo(MINIMUM_NUMBER_OF_OCCUPANTS_OR_USERS_FOR_ASSEMBLY_BUILDING) >= 0) {

				if (DxfFileConstants.AUDITORIUM.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.BANQUET_HALL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.CINEMA.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.CLUB.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.MUSIC_PAVILIONS.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.COMMUNITY_HALL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SCIENCE_CENTRE_OR_MUSEUM.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.CONFERNCE_HALL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.CONVENTION_HALL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SCULPTURE_COMPLEX.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.CULTURAL_COMPLEX.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.EXHIBITION_CENTER.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.GYMNASIA.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.MARRIAGE_HALL_OR_KALYAN_MANDAP
								.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.MULTIPLEX.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.MUSUEM.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.PLACE_OF_WORKSHIP.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.PUBLIC_LIBRARIES.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.RECREATION_BLDG.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SPORTS_COMPLEX.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.STADIUM.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.THEATRE.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.RELIGIOUS_BUILDING.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.RESTAURANT.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SHOPPING_MALL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SHOWROOM.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SUPERMARKETS.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.FOOD_COURTS.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.AIRPORT.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.METRO_STATION.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.BUS_TERMINAL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.ISBT.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.RAILWAY_STATION.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.TRUCK_TERMINAL.equals(occupancyTypeHelper.getSubtype().getCode())) {
					isAssemblyBuilding = true;
				}
			}
		}
		pl.getPlanInformation().setAssemblyBuilding(isAssemblyBuilding);
		return isAssemblyBuilding;
	}

	public static boolean isAssemblyBuildingCriteria(Plan pl, Block block) {
		boolean isAssemblyBuilding = false;
		OccupancyTypeHelper occupancyTypeHelper = pl.getVirtualBuilding().getMostRestrictiveFarHelper();

		if (DxfFileConstants.OC_PUBLIC_SEMI_PUBLIC_OR_INSTITUTIONAL.equals(occupancyTypeHelper.getType().getCode())
				|| DxfFileConstants.OC_COMMERCIAL.equals(occupancyTypeHelper.getType().getCode())
				|| DxfFileConstants.OC_TRANSPORTATION.equals(occupancyTypeHelper.getType().getCode())) {

			// BigDecimal providedNumberOfOccupantsOrUser =
			// pl.getPlanInformation().getNumberOfOccupantsOrUsers();
			BigDecimal providedNumberOfOccupantsOrUser = BigDecimal.ZERO;
			if (block.getNumberOfOccupantsOrUsersOrBedBlk() != null)
				providedNumberOfOccupantsOrUser = providedNumberOfOccupantsOrUser
						.add(block.getNumberOfOccupantsOrUsersOrBedBlk());

			if (providedNumberOfOccupantsOrUser
					.compareTo(MINIMUM_NUMBER_OF_OCCUPANTS_OR_USERS_FOR_ASSEMBLY_BUILDING) >= 0) {

				if (DxfFileConstants.AUDITORIUM.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.BANQUET_HALL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.CINEMA.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.CLUB.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.MUSIC_PAVILIONS.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.COMMUNITY_HALL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SCIENCE_CENTRE_OR_MUSEUM.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.CONFERNCE_HALL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.CONVENTION_HALL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SCULPTURE_COMPLEX.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.CULTURAL_COMPLEX.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.EXHIBITION_CENTER.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.GYMNASIA.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.MARRIAGE_HALL_OR_KALYAN_MANDAP
								.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.MULTIPLEX.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.MUSUEM.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.PLACE_OF_WORKSHIP.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.PUBLIC_LIBRARIES.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.RECREATION_BLDG.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SPORTS_COMPLEX.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.STADIUM.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.THEATRE.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.RELIGIOUS_BUILDING.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.RESTAURANT.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SHOPPING_MALL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SHOWROOM.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.SUPERMARKETS.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.FOOD_COURTS.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.AIRPORT.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.METRO_STATION.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.BUS_TERMINAL.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.ISBT.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.RAILWAY_STATION.equals(occupancyTypeHelper.getSubtype().getCode())
						|| DxfFileConstants.TRUCK_TERMINAL.equals(occupancyTypeHelper.getSubtype().getCode())) {
					isAssemblyBuilding = true;
				}
			}
		}
		block.setAssemblyBuilding(isAssemblyBuilding);
		return isAssemblyBuilding;
	}

	public static BigDecimal getMaxBuildingHeight(Plan pl) {
		BigDecimal buildingHeight = BigDecimal.ZERO;
		try {
			buildingHeight = pl.getBlocks().stream().map(block -> block.getBuilding().getBuildingHeight())
					.reduce(BigDecimal::max).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildingHeight;
	}

	public static void validateServiceFloor(Plan pl, Block b, Floor f) {
		Map<String, Integer> heightOfRoomFeaturesColor = pl.getSubFeatureColorCodesMaster().get("HeightOfRoom");
		boolean isServiceFloor = false;
		String color = DxfFileConstants.COLOR_SERVICE_FLOOR;// 39

		BigDecimal noOfFloorsAboveGround = BigDecimal.ZERO;
		for (Floor floor : b.getBuilding().getFloors()) {
			if (floor.getNumber() != null && floor.getNumber() >= 0) {
				noOfFloorsAboveGround = noOfFloorsAboveGround.add(BigDecimal.valueOf(1));
			}
		}

		boolean hasTerrace = b.getBuilding().getFloors().stream()
				.anyMatch(floor -> floor.getTerrace().equals(Boolean.TRUE));

		noOfFloorsAboveGround = hasTerrace ? noOfFloorsAboveGround.subtract(BigDecimal.ONE) : noOfFloorsAboveGround;

		BigDecimal totalArea = BigDecimal.ZERO;
		BigDecimal height = BigDecimal.ZERO;
		for (Room room : f.getRegularRooms()) {
			for (Measurement measurement : room.getRooms()) {
				if (heightOfRoomFeaturesColor.get(color) == measurement.getColorCode()) {
					isServiceFloor = true;
					totalArea = totalArea.add(measurement.getArea());
					height = measurement.getHeight();
					break;
				}
			}
			if (isServiceFloor) {
				if (noOfFloorsAboveGround.compareTo(new BigDecimal("4")) <= 0)
					pl.addError("SERVICE_FLOOR", "Service Floor not allowed in less then 5 story building");
				if (f.getNumber() <= 0)
					pl.addError("SERVICE_FLOOR1", "Service Floor not allowed on floor 0 or besment");
			}
		}
		f.setIsServiceFloor(isServiceFloor);
		totalArea = roundUp(totalArea);
		f.setTotalServiceArea(totalArea);
		height = roundUp(height);
		f.setServiceFloorHeight(height);
	}

	public static void validateStilledFloor(Plan pl, Block b, Floor f) {
		Map<String, Integer> heightOfRoomFeaturesColor = pl.getSubFeatureColorCodesMaster().get("HeightOfRoom");
		boolean isStiltFloor = false;
		String color = DxfFileConstants.COLOR_STILT_FLOOR;// 38
		BigDecimal totalStilledArea = BigDecimal.ZERO;
		BigDecimal flrHeight = BigDecimal.ZERO;
		for (Room room : f.getRegularRooms()) {
			for (Measurement measurement : room.getRooms()) {
				if (heightOfRoomFeaturesColor.get(color) == measurement.getColorCode()) {
					isStiltFloor = true;
					totalStilledArea = totalStilledArea.add(measurement.getArea());
				}
			}
			for (RoomHeight roomHeight : room.getHeights()) {
				if (heightOfRoomFeaturesColor.get(color) == roomHeight.getColorCode()) {
					if (flrHeight.compareTo(roomHeight.getHeight()) < 0)
						flrHeight = roomHeight.getHeight();
				}
			}

			if (isStiltFloor && f.getNumber() < 0) {
				pl.addError("STILT_FLOOR", "Stilt Floor can not be in besment.");
			}
		}

		// deducted building height need to test and verify
		if (isStiltFloor) {
			if (b.getBuilding().getDeclaredBuildingHeight().compareTo(new BigDecimal("15")) < 0) {
				if (flrHeight.compareTo(new BigDecimal("2.4")) == 0)
					b.getBuilding()
							.setBuildingHeight(b.getBuilding().getBuildingHeight().subtract(new BigDecimal("2.4")));
				;
			} else {
				b.getBuilding().setBuildingHeight(b.getBuilding().getBuildingHeight().subtract(f.getHeight()));
			}
		}

		f.setIsStiltFloor(isStiltFloor);
		totalStilledArea = roundUp(totalStilledArea);
		f.setTotalStiltArea(totalStilledArea);
		flrHeight = roundUp(flrHeight);
		f.setStiltFloorHeight(flrHeight);
	}

	public static void validateHeightOfTheCeilingOfUpperBasementDeduction(Plan pl, Block b, Floor f) {
		if (f != null && f.getNumber() == -1) {
			BigDecimal maxLength = BigDecimal.ZERO;
			try {
				maxLength = f.getHeightOfTheCeilingOfUpperBasement().stream().reduce(BigDecimal::max).get();
			} catch (Exception e) {
				// TODO: handle exception
			}
			b.getBuilding().setBuildingHeight(b.getBuilding().getBuildingHeight().subtract(maxLength));
			;
		}
	}

	public static BigDecimal roundUp(BigDecimal number) {
		number = number.setScale(2, BigDecimal.ROUND_HALF_UP);
		return number;
	}

	public static void setPlanInfoBlkWise(Plan pl, String key) {
		BigDecimal totalUserInPlan = BigDecimal.ZERO;
		for (Block block : pl.getBlocks()) {
			String value = pl.getPlanInfoProperties().get(key + "_" + block.getNumber());
			String glassFacadeOpening = pl.getPlanInfoProperties()
					.get(DxfFileConstants.IS_BLOCK_S_HAVING_ENTIRE_FACADE_IN_GLASS.replace("%S", block.getNumber()));
			try {
				BigDecimal numValue = new BigDecimal(value);
				block.setNumberOfOccupantsOrUsersOrBedBlk(numValue);
				totalUserInPlan.add(numValue);
				block.setGlassFacadeOpening(DxfFileConstants.YES.equals(glassFacadeOpening) ? true : false);
				if (numValue.compareTo(BigDecimal.ZERO) <= 0)
					pl.addError("NUMBER_OF_OCCUPANTS_OR_USERS_" + block.getNumber(),
							"Number Of Occupants/Users/Bed is not defined in block " + block.getNumber());
			} catch (Exception e) {
				pl.addError("NUMBER_OF_OCCUPANTS_OR_USERS_" + block.getNumber(),
						"Number Of Occupants/Users/Bed is invalid in block " + block.getNumber());
			}
		}
		pl.getPlanInformation().setNumberOfOccupantsOrUsers(totalUserInPlan);
	}

	public static void updateDUnitInPlan(Plan pl) {
		long totalDU = 0;
		for (Block block : pl.getBlocks()) {
			for (Floor floor : block.getBuilding().getFloors()) {
				List<FloorUnit> ews = new ArrayList<>();
				List<FloorUnit> lig = new ArrayList<>();
				List<FloorUnit> mig1 = new ArrayList<>();
				List<FloorUnit> mig2 = new ArrayList<>();
				List<FloorUnit> other = new ArrayList<>();
				List<FloorUnit> room = new ArrayList<>();
				List<FloorUnit> ownersSocietyOffice = new ArrayList<>();
				for (FloorUnit floorUnit : floor.getUnits()) {
					switch (floorUnit.getColorCode()) {
					case COLOR_EWS:
						ews.add(floorUnit);
						totalDU++;
						break;
					case COLOR_LIG:
						lig.add(floorUnit);
						totalDU++;
						break;
					case COLOR_MIG1:
						mig1.add(floorUnit);
						totalDU++;
						break;
					case COLOR_MIG2:
						mig2.add(floorUnit);
						totalDU++;
						break;
					case COLOR_OTHER:
						other.add(floorUnit);
						totalDU++;
						break;
					case COLOR_ROOM:
						room.add(floorUnit);
						totalDU++;
						break;
					case COLOR_OWNERS_SOCIETY_OFFICE:
						ownersSocietyOffice.add(floorUnit);
						break;
					}
				}
				floor.setEwsUnit(ews);
				floor.setLigUnit(lig);
				floor.setMig1Unit(mig1);
				floor.setMig2Unit(mig2);
				floor.setOthersUnit(other);
				floor.setRoomUnit(room);
				floor.setOwnersSocietyOffice(ownersSocietyOffice);
			}
		}
		pl.getPlanInformation().setTotalNoOfDwellingUnits(totalDU);
	}

	public static BigDecimal getTotalTopMostRoofArea(Plan pl) {
		BigDecimal totalArea = BigDecimal.ZERO;

//		for (Block block : pl.getBlocks()) {
//			for (Floor floor : block.getBuilding().getFloors()) {
//				try {
//					BigDecimal area = floor.getRoofAreas().stream().map(roofArea -> roofArea.getArea())
//							.reduce(BigDecimal::add).get();
//					totalArea = totalArea.add(area);
//				} catch (Exception exception) {
//
//				}
//			}
//		}

		for (Block block : pl.getBlocks()) {
			List<Floor> floors = block.getBuilding().getFloors();
			if (floors != null && !floors.isEmpty()) {
				Floor lastFloor = floors.get(floors.size() - 1);
				BigDecimal area = BigDecimal.ZERO;
				try {
					area = lastFloor.getRoofAreas().stream().map(roofArea -> roofArea.getArea()).reduce(BigDecimal::add)
							.get();
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (area == null || area.compareTo(BigDecimal.ZERO) <= 0) {
					pl.addError("RoofArea", "RoofArea is not defined in block " + block.getNumber());
				}
				totalArea = totalArea.add(area);
			}

		}

		return totalArea;
	}

	public static BigDecimal getTotalRoofArea(Plan pl) {
		BigDecimal totalArea = BigDecimal.ZERO;

		for (Block block : pl.getBlocks()) {
			for (Floor floor : block.getBuilding().getFloors()) {
				try {
					BigDecimal area = floor.getRoofAreas().stream().map(roofArea -> roofArea.getArea())
							.reduce(BigDecimal::add).get();
					totalArea = totalArea.add(area);
				} catch (Exception exception) {

				}
			}
		}

		return totalArea;
	}

	public static List<Room> getRegularRoom(Plan pl, List<Room> rooms, Set<String> allowedRooms) {
		Map<String, Integer> heightOfRoomFeaturesColor = pl.getSubFeatureColorCodesMaster().get("HeightOfRoom");
		List<Integer> allowedRoomColorCode = new ArrayList<>();
		for (String allowedRoom : allowedRooms) {
			allowedRoomColorCode.add(heightOfRoomFeaturesColor.get(allowedRoom));
		}
		List<Room> spcRoom = new ArrayList<Room>();
		if (rooms != null) {
			for (Room room : rooms) {
				List<Measurement> measurements = new ArrayList<>();
				List<RoomHeight> heightOfRooms = new ArrayList<>();
				if (room.getRooms() != null && !room.getRooms().isEmpty() && room.getRooms().size() >= 1) {
					for (Measurement r : room.getRooms()) {
						if (allowedRoomColorCode.contains(r.getColorCode())) {
							measurements.add(r);
						}
					}
					for (RoomHeight roomHeight : room.getHeights()) {
						if (allowedRoomColorCode.contains(roomHeight.getColorCode())) {
							RoomHeight height = new RoomHeight();
							height.setColorCode(roomHeight.getColorCode());
							height.setHeight(roomHeight.getHeight());
							heightOfRooms.add(height);
						}
					}
					// lightAndVentilation
					Room room2 = new Room();
					room2.setNumber(room.getNumber());
					room2.setHeights(heightOfRooms);
					room2.setClosed(room.getClosed());
					room2.setRooms(measurements);
					room2.setLightAndVentilation(room.getLightAndVentilation());
					room2.setMezzanineAreas(room.getMezzanineAreas());
					spcRoom.add(room2);
				}

			}
		}
		return spcRoom;
	}

	public static BigDecimal getRoofTopParking(Plan pl) {
		ParkingDetails details = pl.getParkingDetails();
		BigDecimal totalParking = BigDecimal.ZERO;
		if (details.getSpecial() != null && !details.getSpecial().isEmpty()) {
			for (Measurement measurement : details.getSpecial()) {
				switch (measurement.getColorCode()) {
				case Parking.COLOR_LAYER_SPECIAL_PARKING_ROOF_TOP_PARKING:
					totalParking = totalParking.add(measurement.getArea()).setScale(2, BigDecimal.ROUND_HALF_UP);
					break;
				}
			}
		}
		return totalParking;
	}

	public static BigDecimal getOpenParking(Plan pl) {
		BigDecimal openParking = BigDecimal.ZERO;
		try {
			openParking = pl.getParkingDetails().getOpenCars().stream().map(Measurement::getArea)
					.reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return openParking;
	}

	public static boolean isEWSOrLIGBlock(Plan pl, Block block) {
		boolean flage = false;
		OccupancyTypeHelper occupancyTypeHelper = pl.getVirtualBuilding().getMostRestrictiveFarHelper();

		if (DxfFileConstants.EWS.equals(occupancyTypeHelper.getSubtype().getCode())
				|| DxfFileConstants.LOW_INCOME_HOUSING.equals(occupancyTypeHelper.getSubtype().getCode())) {
			return true;
		} else {
			for (Floor floor : block.getBuilding().getFloors()) {
				for (Occupancy occupancy : floor.getOccupancies()) {
					OccupancyTypeHelper helper = occupancy.getTypeHelper();
					if (helper != null && helper.getSubtype() != null
							&& (DxfFileConstants.EWS.equals(helper.getSubtype().getCode())
									|| DxfFileConstants.LOW_INCOME_HOUSING.equals(helper.getSubtype().getCode()))) {
						return true;
					}

				}
			}
		}
		return flage;
	}

	public static boolean isSpecialBuilding(Plan pl) {
		boolean specialBuilding = false;

		boolean isAssemblyBuilding = false;
		for (Block block : pl.getBlocks()) {
			if (block.isAssemblyBuilding()) {
				isAssemblyBuilding = true;
				break;
			}
		}

		boolean isHazardousBuildings = false;
		if (DxfFileConstants.YES.equals(pl.getPlanInformation().getBuildingUnderHazardousOccupancyCategory()))
			isHazardousBuildings = true;

		boolean isBuildingCentrallyAirConditioned = false;
		if (DxfFileConstants.YES.equals(pl.getPlanInformation().getBuildingCentrallyAirConditioned())) {
			if (pl.getVirtualBuilding().getTotalBuitUpArea().compareTo(new BigDecimal("500")) > 0)
				isBuildingCentrallyAirConditioned = true;
		}

		OccupancyTypeHelper occupancyTypeHelper = pl.getVirtualBuilding().getMostRestrictiveFarHelper();
		boolean isSplOccupancy = false;
		if (DxfFileConstants.OC_INDUSTRIAL_ZONE.equals(occupancyTypeHelper.getType().getCode())
				|| DxfFileConstants.WHOLESALE_STORAGE_NON_PERISHABLE.equals(occupancyTypeHelper.getSubtype().getCode())
				|| DxfFileConstants.WHOLESALE_STORAGE_PERISHABLE.equals(occupancyTypeHelper.getSubtype().getCode())
				|| DxfFileConstants.WHOLESALE_MARKET.equals(occupancyTypeHelper.getSubtype().getCode())
				|| DxfFileConstants.HOTEL.equals(occupancyTypeHelper.getSubtype().getCode())
				|| DxfFileConstants.FIVE_STAR_HOTEL.equals(occupancyTypeHelper.getSubtype().getCode()))
			isSplOccupancy = true;

		boolean isMixedOccupancies = false;// need to add condition

		if (isAssemblyBuilding || isHazardousBuildings || isBuildingCentrallyAirConditioned || isSplOccupancy
				|| isMixedOccupancies)
			specialBuilding = true;

		return specialBuilding;
	}

	public static void updateBlock(Plan pl) {
		List<Block> outhouses = new ArrayList<>();
		List<Block> blocks = new ArrayList<>();

		for (Block block : pl.getBlocks()) {
			boolean flage = false;
			for (Floor floor : block.getBuilding().getFloors()) {
				for (Occupancy occupancy : floor.getOccupancies()) {
					if (occupancy.getTypeHelper() != null && occupancy.getTypeHelper().getSubtype() != null
							&& DxfFileConstants.OUTHOUSE.equals(occupancy.getTypeHelper().getSubtype().getCode())) {
						flage = true;
					}
				}
				if (flage) {
					break;
				}
			}
			if (flage) {
				block.setOutHouse(flage);
				outhouses.add(block);
				break;
			} else {
				blocks.add(block);
			}
		}
		pl.setBlocks(blocks);
		pl.setOuthouse(outhouses);
	}

	public static BigDecimal getNumberOfPerson(Plan pl) {
		OccupancyTypeHelper mostRestrictiveOccupancyType = pl.getVirtualBuilding().getMostRestrictiveFarHelper();
		BigDecimal numberOfPerson = BigDecimal.ZERO;
		for (Block block : pl.getBlocks()) {
			for (Floor floor : block.getBuilding().getFloors()) {
				numberOfPerson.add(getNumberPerson(floor.getArea(), mostRestrictiveOccupancyType, floor.getNumber()));
			}
		}

		return numberOfPerson;

	}

	private static BigDecimal getNumberPerson(BigDecimal bulidUpArea, OccupancyTypeHelper mostRestrictiveOccupancyType,
			int floor) {
		BigDecimal numberOfPerson = BigDecimal.ZERO;
		BigDecimal perPersonBuildupArea = BigDecimal.ZERO;
		if (DxfFileConstants.OC_RESIDENTIAL.equals(mostRestrictiveOccupancyType.getType().getCode())
				|| DxfFileConstants.OC_AGRICULTURE.equals(mostRestrictiveOccupancyType.getType().getCode()))
			perPersonBuildupArea = BigDecimal.valueOf(12.5);
		else if (DxfFileConstants.OC_INDUSTRIAL_ZONE.equals(mostRestrictiveOccupancyType.getType().getCode())
				|| DxfFileConstants.OC_TRANSPORTATION.equals(mostRestrictiveOccupancyType.getType().getCode())
				|| DxfFileConstants.WHOLESALE_STORAGE_PERISHABLE
						.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.WHOLESALE_STORAGE_NON_PERISHABLE
						.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.STORAGE_OR_HANGERS_OR_TERMINAL_DEPOT
						.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.WARE_HOUSE.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.GOOD_STORAGE.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.GODOWNS.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.GAS_GODOWN.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.HOTEL.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.FIVE_STAR_HOTEL.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.MOTELS.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.BANK.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.RESORTS.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.LAGOONS_AND_LAGOON_RESORT
						.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.AMUSEMENT_BUILDING_OR_PARK_AND_WATER_SPORTS
						.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.FINANCIAL_SERVICES_AND_STOCK_EXCHANGES
						.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.COMMERCIAL_AND_BUSINESS_OFFICES_OR_COMPLEX
						.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.PROFESSIONAL_OFFICES.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.HOLIDAY_RESORT.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.GUEST_HOUSES.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.BOARDING_AND_LODGING_HOUSES
						.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.RESTAURANT.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.PETROL_PUMP_FILLING_STATION_AND_SERVICE_STATION
						.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.PETROL_PUMP_ONLY_FILLING_STATION
						.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.CNG_MOTHER_STATION.equals(mostRestrictiveOccupancyType.getSubtype().getCode())
				|| DxfFileConstants.WEIGH_BRIDGES.equals(mostRestrictiveOccupancyType.getSubtype().getCode()))
			perPersonBuildupArea = BigDecimal.valueOf(10);
		else if (DxfFileConstants.OC_PUBLIC_SEMI_PUBLIC_OR_INSTITUTIONAL
				.equals(mostRestrictiveOccupancyType.getType().getCode())
				|| DxfFileConstants.OC_PUBLIC_UTILITY.equals(mostRestrictiveOccupancyType.getType().getCode()))
			perPersonBuildupArea = BigDecimal.valueOf(15);
		else if (DxfFileConstants.OC_EDUCATION.equals(mostRestrictiveOccupancyType.getType().getCode()))
			perPersonBuildupArea = BigDecimal.valueOf(4);
		else if (DxfFileConstants.OC_COMMERCIAL.equals(mostRestrictiveOccupancyType.getType().getCode())) {
			if (floor <= 0)
				perPersonBuildupArea = BigDecimal.valueOf(1);
			else
				perPersonBuildupArea = BigDecimal.valueOf(6);

		}

		numberOfPerson = perPersonBuildupArea.divide(perPersonBuildupArea);

		return new BigDecimal(String.format("%.0f", numberOfPerson));

	}

	private static final int COLOR_AMMENITY_GUARD_ROOM = 1;
	private static final int COLOR_ELECTRIC_CABIN = 2;
	private static final int COLOR_SUB_STATION = 3;
	private static final int COLOR_AREA_FOR_GENERATOR_SET = 4;
	private static final int COLOR_ATM = 5;
	private static final int COLOR_OTHER_AMMENITY = 6;

	public static void updateAmmenity(Plan pl) {
		Ammenity ammenity=new Ammenity();
		for (AccessoryBlock accessoryBlock : pl.getAccessoryBlocks()) {
			for(Measurement measurement:accessoryBlock.getAccessoryBuilding().getUnits()) {
				switch (measurement.getColorCode()) {
				case COLOR_AMMENITY_GUARD_ROOM:
					ammenity.getGuardRooms().add(measurement);
					break;
				case COLOR_ELECTRIC_CABIN:
					ammenity.getElectricCabins().add(measurement);
					break;
				case COLOR_SUB_STATION:
					ammenity.getSubStations().add(measurement);
					break;
				case COLOR_AREA_FOR_GENERATOR_SET:
					ammenity.getAreaForGeneratorSet().add(measurement);
					break;
				case COLOR_ATM:
					ammenity.getAtms().add(measurement);
					break;
				case COLOR_OTHER_AMMENITY:
					ammenity.getOtherAmmenities().add(measurement);
					break;
				}
			}
		}
		
		pl.setAmmenity(ammenity);

	}

}